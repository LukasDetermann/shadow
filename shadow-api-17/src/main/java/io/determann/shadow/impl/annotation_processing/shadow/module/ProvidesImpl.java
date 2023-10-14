package io.determann.shadow.impl.annotation_processing.shadow.module;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.module.DirectiveKind;
import io.determann.shadow.api.shadow.module.Provides;

import javax.lang.model.element.ModuleElement;
import java.util.List;
import java.util.Objects;

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
      return MirrorAdapter.getShadow(getApi(), providesDirective.getService());
   }

   @Override
   public List<Declared> getImplementations()
   {
      return providesDirective.getImplementations()
                              .stream()
                              .map(typeElement -> MirrorAdapter.<Declared>getShadow(getApi(), typeElement))
                              .toList();
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
