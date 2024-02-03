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
      return to(DirectiveKind.EXPORTS);
   }

   @Override
   public Optional<Exports> toExports()
   {
      return toOptional(DirectiveKind.EXPORTS);
   }

   @Override
   public Opens toOpensOrThrow()
   {
      return to(DirectiveKind.OPENS);
   }

   @Override
   public Optional<Opens> toOpens()
   {
      return toOptional(DirectiveKind.OPENS);
   }

   @Override
   public Provides toProvidesOrThrow()
   {
      return to(DirectiveKind.PROVIDES);
   }

   @Override
   public Optional<Provides> toProvides()
   {
      return toOptional(DirectiveKind.PROVIDES);
   }

   @Override
   public Requires toRequiresOrThrow()
   {
      return to(DirectiveKind.REQUIRES);
   }

   @Override
   public Optional<Requires> toRequires()
   {
      return toOptional(DirectiveKind.REQUIRES);
   }

   @Override
   public Uses toUsesOrThrow()
   {
      return to(DirectiveKind.USES);
   }

   @Override
   public Optional<Uses> toUses()
   {
      return toOptional(DirectiveKind.USES);
   }

   private <DIRECTIVE extends Directive> DIRECTIVE to(DirectiveKind directiveKind)
   {
      //noinspection unchecked
      return (DIRECTIVE) toOptional(directiveKind).orElseThrow(() -> new IllegalStateException(directive.getKind() +
                                                                                               " is not a " +
                                                                                               directiveKind));
   }

   private <DIRECTIVE extends Directive> Optional<DIRECTIVE> toOptional(DirectiveKind directiveKind)
   {
      if (directiveKind.equals(directive.getKind()))
      {
         //noinspection unchecked
         return Optional.of((DIRECTIVE) directive);
      }
      return Optional.empty();
   }
}
