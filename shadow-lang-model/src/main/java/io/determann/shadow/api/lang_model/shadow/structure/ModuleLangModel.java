package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.DocumentedLangModel;
import io.determann.shadow.api.lang_model.shadow.NameableLangModel;
import io.determann.shadow.api.lang_model.shadow.QualifiedNameableLamgModel;
import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;
import io.determann.shadow.api.shadow.module.Directive;
import io.determann.shadow.api.shadow.module.DirectiveConsumer;
import io.determann.shadow.api.shadow.module.DirectiveMapper;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;

import java.util.List;

import static io.determann.shadow.api.converter.Converter.convert;

public interface ModuleLangModel extends Module,
                                         ShadowLangModel,
                                         NameableLangModel,
                                         QualifiedNameableLamgModel,
                                         DocumentedLangModel
{
   List<Package> getPackages();

   /**
    * can everybody use reflection on this module?
    */
   boolean isOpen();

   boolean isUnnamed();

   boolean isAutomatic();

   /**
    * Relations between modules
    */
   List<Directive> getDirectives();

   default <T> List<T> mapDirectives(DirectiveMapper<T> mapper)
   {
      return getDirectives().stream()
                            .map(directive ->
                                       switch (directive.getKind())
                                       {
                                          case REQUIRES -> mapper.requires(convert(directive).toRequiresOrThrow());
                                          case EXPORTS -> mapper.exports(convert(directive).toExportsOrThrow());
                                          case OPENS -> mapper.opens(convert(directive).toOpensOrThrow());
                                          case USES -> mapper.uses(convert(directive).toUsesOrThrow());
                                          case PROVIDES -> mapper.provides(convert(directive).toProvidesOrThrow());
                                       })
                            .toList();
   }

   default void consumeDirectives(DirectiveConsumer consumer)
   {
      getDirectives().forEach(directive ->
                              {
                                 switch (directive.getKind())
                                 {
                                    case REQUIRES -> consumer.requires(convert(directive).toRequiresOrThrow());
                                    case EXPORTS -> consumer.exports(convert(directive).toExportsOrThrow());
                                    case OPENS -> consumer.opens(convert(directive).toOpensOrThrow());
                                    case USES -> consumer.uses(convert(directive).toUsesOrThrow());
                                    case PROVIDES -> consumer.provides(convert(directive).toProvidesOrThrow());
                                 }
                              });
   }
}
