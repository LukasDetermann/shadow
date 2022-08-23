package org.determann.shadow.impl.shadow.module;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.shadow.Declared;
import org.determann.shadow.api.shadow.module.DirectiveKind;
import org.determann.shadow.api.shadow.module.Provides;

import javax.lang.model.element.ModuleElement;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toUnmodifiableList;

public class ProvidesImpl extends DirectiveImpl implements Provides
{
   private final ModuleElement.ProvidesDirective providesDirective;

   public ProvidesImpl(ShadowApi shadowApi, ModuleElement.ProvidesDirective providesDirective)
   {
      super(shadowApi);
      this.providesDirective = providesDirective;
   }

   @Override
   public Declared getService()
   {
      return shadowApi.getShadowFactory().shadowFromElement(providesDirective.getService());
   }

   @Override
   public List<Declared> getImplementations()
   {
      return providesDirective.getImplementations()
                              .stream()
                              .map(typeElement -> shadowApi.getShadowFactory().<Declared>shadowFromElement(typeElement))
                              .collect(toUnmodifiableList());
   }

   @Override
   public DirectiveKind getKind()
   {
      return DirectiveKind.PROVIDES;
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (other == null || !getClass().equals(other.getClass()))
      {
         return false;
      }
      ProvidesImpl otherProvides = (ProvidesImpl) other;
      return Objects.equals(getKind(), otherProvides.getKind()) &&
             Objects.equals(getImplementations(), otherProvides.getImplementations()) &&
             Objects.equals(getService(), otherProvides.getService());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getKind(), getService(), getImplementations());
   }

   @Override
   public String toString()
   {
      return providesDirective.toString();
   }
}
