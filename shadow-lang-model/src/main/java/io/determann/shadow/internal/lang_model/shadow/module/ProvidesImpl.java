package io.determann.shadow.internal.lang_model.shadow.module;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.module.DirectiveKind;
import io.determann.shadow.api.shadow.module.Provides;

import javax.lang.model.element.ModuleElement;
import java.util.List;
import java.util.Objects;

public class ProvidesImpl extends DirectiveImpl implements Provides
{
   private final ModuleElement.ProvidesDirective providesDirective;

   public ProvidesImpl(LangModelContext context, ModuleElement.ProvidesDirective providesDirective)
   {
      super(context);
      this.providesDirective = providesDirective;
   }

   @Override
   public Declared getService()
   {
      return LangModelAdapter.generalize(getApi(), providesDirective.getService());
   }

   @Override
   public List<Declared> getImplementations()
   {
      return providesDirective.getImplementations()
                              .stream()
                              .map(typeElement -> LangModelAdapter.<Declared>generalize(getApi(), typeElement))
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
      if (!(other instanceof Provides otherProvides))
      {
         return false;
      }
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
