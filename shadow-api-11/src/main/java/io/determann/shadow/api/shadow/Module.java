package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.DeclaredHolder;
import io.determann.shadow.api.QualifiedNameable;
import io.determann.shadow.api.shadow.module.Directive;
import io.determann.shadow.api.shadow.module.DirectiveConsumer;
import io.determann.shadow.api.shadow.module.DirectiveMapper;
import io.determann.shadow.api.shadow.module.Provides;

import java.util.List;

import static io.determann.shadow.api.converter.Converter.convert;
import static java.util.stream.Collectors.toUnmodifiableList;

public interface Module extends Shadow,
                                QualifiedNameable,
                                Annotationable,
                                DeclaredHolder
{
   List<Package> getPackages();

   /**
    * can everybody use reflection on this module?
    */
   boolean isOpen();

   boolean isUnnamed();

   /**
    * Relations between modules
    * <p>
    * Different behavior for {@link io.determann.shadow.api.shadow.module.Provides}!
    * The jdk api returns redundant entries for {@link io.determann.shadow.api.shadow.module.Provides} with multiple {@link Provides#getImplementations()} for {@code java.desktop} for example:
    * <pre>{@code
    * provides javax.sound.midi.spi.MidiDeviceProvider with com.sun.media.sound.MidiInDeviceProvider;
    * provides javax.sound.midi.spi.MidiDeviceProvider with com.sun.media.sound.MidiInDeviceProvider, com.sun.media.sound.MidiOutDeviceProvider;
    * provides javax.sound.midi.spi.MidiDeviceProvider with com.sun.media.sound.MidiInDeviceProvider, com.sun.media.sound.MidiOutDeviceProvider, com.sun.media.sound.RealTimeSequencerProvider;
    * provides javax.sound.midi.spi.MidiDeviceProvider with com.sun.media.sound.MidiInDeviceProvider, com.sun.media.sound.MidiOutDeviceProvider, com.sun.media.sound.RealTimeSequencerProvider, com.sun.media.sound.SoftProvider;
    * }</pre>
    * this method will only return the complete entry
    * <pre>{@code
    * provides javax.sound.midi.spi.MidiDeviceProvider with com.sun.media.sound.MidiInDeviceProvider, com.sun.media.sound.MidiOutDeviceProvider, com.sun.media.sound.RealTimeSequencerProvider, com.sun.media.sound.SoftProvider;
    * }</pre>
    */
   List<Directive> getDirectives();

   default <T> List<T> mapDirectives(DirectiveMapper<T> mapper)
   {
      return getDirectives().stream()
                            .map(directive ->
                                 {
                                    switch (directive.getKind())
                                    {
                                       case REQUIRES:
                                          return mapper.requires(convert(directive).toRequiresOrThrow());
                                       case EXPORTS:
                                          return mapper.exports(convert(directive).toExportsOrThrow());
                                       case OPENS:
                                          return mapper.opens(convert(directive).toOpensOrThrow());
                                       case USES:
                                          return mapper.uses(convert(directive).toUsesOrThrow());
                                       case PROVIDES:
                                          return mapper.provides(convert(directive).toProvidesOrThrow());
                                       default:
                                          throw new IllegalArgumentException();
                                    }
                                 })
                            .collect(toUnmodifiableList());
   }

   default void consumeDirectives(DirectiveConsumer consumer)
   {
      getDirectives().forEach(directive ->
                              {
                                 switch (directive.getKind())
                                 {
                                    case REQUIRES:
                                       consumer.requires(convert(directive).toRequiresOrThrow());
                                       break;
                                    case EXPORTS:
                                       consumer.exports(convert(directive).toExportsOrThrow());
                                       break;
                                    case OPENS:
                                       consumer.opens(convert(directive).toOpensOrThrow());
                                       break;
                                    case USES:
                                       consumer.uses(convert(directive).toUsesOrThrow());
                                       break;
                                    case PROVIDES:
                                       consumer.provides(convert(directive).toProvidesOrThrow());
                                       break;
                                 }
                              });
   }
}
