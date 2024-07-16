package io.determann.shadow.internal.converter;

import io.determann.shadow.api.converter.module.*;
import io.determann.shadow.api.shadow.module.*;

import java.util.Optional;

public class DirectiveConverterImpl implements DirectiveConverter,
                                               ExportsConverter,
                                               OpensConverter,
                                               ProvidesConverter,
                                               RequiresConverter,
                                               UsesConverter
{

   private final Directive directive;

   public DirectiveConverterImpl(Directive directive)
   {
      this.directive = directive;
   }

   @Override
   public Exports toExportsOrThrow()
   {
      return to(Exports.class);
   }

   @Override
   public Optional<Exports> toExports()
   {
      return toOptional(Exports.class);
   }

   @Override
   public Opens toOpensOrThrow()
   {
      return to(Opens.class);
   }

   @Override
   public Optional<Opens> toOpens()
   {
      return toOptional(Opens.class);
   }

   @Override
   public Provides toProvidesOrThrow()
   {
      return to(Provides.class);
   }

   @Override
   public Optional<Provides> toProvides()
   {
      return toOptional(Provides.class);
   }

   @Override
   public Requires toRequiresOrThrow()
   {
      return to(Requires.class);
   }

   @Override
   public Optional<Requires> toRequires()
   {
      return toOptional(Requires.class);
   }

   @Override
   public Uses toUsesOrThrow()
   {
      return to(Uses.class);
   }

   @Override
   public Optional<Uses> toUses()
   {
      return toOptional(Uses.class);
   }

   private <DIRECTIVE extends Directive> DIRECTIVE to(Class<DIRECTIVE> directiveType)
   {
      return toOptional(directiveType).orElseThrow(() -> new IllegalStateException(directive +
                                                                                   " can not be converted to " +
                                                                                   directiveType));
   }

   private <DIRECTIVE extends Directive> Optional<DIRECTIVE> toOptional(Class<DIRECTIVE> directiveType)
   {
      if (directiveType.isInstance(directive))
      {
         //noinspection unchecked
         return Optional.of((DIRECTIVE) directive);
      }
      return Optional.empty();
   }
}
