package org.determann.shadow.impl.converter;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.converter.module.*;
import org.determann.shadow.api.shadow.module.*;
import org.determann.shadow.impl.shadow.module.*;

import javax.lang.model.element.ModuleElement;
import java.util.Optional;

public class DirectiveConverterImpl implements DirectiveConverter,
                                               ExportsConverter,
                                               OpensConverter,
                                               ProvidesConverter,
                                               RequiresConverter,
                                               UsesConverter
{

   private final ShadowApi shadowApi;
   private final Directive directive;

   public DirectiveConverterImpl(ShadowApi shadowApi, Directive directive)
   {
      this.shadowApi = shadowApi;
      this.directive = directive;
   }

   @Override
   public Optional<Exports> toOptionalExports()
   {
      if (directive.getKind().equals(DirectiveKind.REQUIRES))
      {
         return Optional.of(new ExportsImpl(shadowApi, ((ModuleElement.ExportsDirective) directive)));
      }
      return Optional.empty();
   }

   @Override
   public Optional<Opens> toOptionalOpens()
   {
      if (directive.getKind().equals(DirectiveKind.REQUIRES))
      {
         return Optional.of(new OpensImpl(shadowApi, ((ModuleElement.OpensDirective) directive)));
      }
      return Optional.empty();
   }

   @Override
   public Optional<Provides> toOptionalProvides()
   {
      if (directive.getKind().equals(DirectiveKind.REQUIRES))
      {
         return Optional.of(new ProvidesImpl(shadowApi, ((ModuleElement.ProvidesDirective) directive)));
      }
      return Optional.empty();
   }

   @Override
   public Optional<Requires> toOptionalRequires()
   {
      if (directive.getKind().equals(DirectiveKind.REQUIRES))
      {
         return Optional.of(new RequiresImpl(shadowApi, ((ModuleElement.RequiresDirective) directive)));
      }
      return Optional.empty();
   }

   @Override
   public Optional<Uses> toOptionalUses()
   {
      if (directive.getKind().equals(DirectiveKind.REQUIRES))
      {
         return Optional.of(new UsesImpl(shadowApi, ((ModuleElement.UsesDirective) directive)));
      }
      return Optional.empty();
   }
}
