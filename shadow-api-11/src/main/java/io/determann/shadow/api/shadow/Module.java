package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.DeclaredHolder;
import io.determann.shadow.api.QualifiedNameable;
import io.determann.shadow.api.shadow.module.Directive;
import io.determann.shadow.api.shadow.module.DirectiveConsumer;
import io.determann.shadow.api.shadow.module.DirectiveMapper;
import io.determann.shadow.api.shadow.module.Provides;

import javax.lang.model.element.ModuleElement;
import java.util.List;

public interface Module extends Shadow,
                                QualifiedNameable<ModuleElement>,
                                Annotationable<ModuleElement>,
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

   <T> List<T> mapDirectives(DirectiveMapper<T> mapper);

   void consumeDirectives(DirectiveConsumer consumer);

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
