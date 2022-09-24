package org.determann.shadow.impl.converter;

import org.determann.shadow.api.converter.module.*;
import org.determann.shadow.api.shadow.module.*;

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
   public Exports toExports()
   {
      return to(DirectiveKind.EXPORTS);
   }

   @Override
   public Optional<Exports> toOptionalExports()
   {
      return toOptional(DirectiveKind.EXPORTS);
   }

   @Override
   public Opens toOpens()
   {
      return to(DirectiveKind.OPENS);
   }

   @Override
   public Optional<Opens> toOptionalOpens()
   {
      return toOptional(DirectiveKind.OPENS);
   }

   @Override
   public Provides toProvides()
   {
      return to(DirectiveKind.PROVIDES);
   }

   @Override
   public Optional<Provides> toOptionalProvides()
   {
      return toOptional(DirectiveKind.PROVIDES);
   }

   @Override
   public Requires toRequires()
   {
      return to(DirectiveKind.REQUIRES);
   }

   @Override
   public Optional<Requires> toOptionalRequires()
   {
      return toOptional(DirectiveKind.REQUIRES);
   }

   @Override
   public Uses toUses()
   {
      return to(DirectiveKind.USES);
   }

   @Override
   public Optional<Uses> toOptionalUses()
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
