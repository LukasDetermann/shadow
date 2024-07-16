package io.determann.shadow.internal.lang_model.shadow.module;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.module.Requires;
import io.determann.shadow.api.shadow.structure.Module;

import javax.lang.model.element.ModuleElement;
import java.util.Objects;

public class RequiresImpl extends DirectiveImpl implements Requires
{
   private final ModuleElement.RequiresDirective requiresDirective;

   public RequiresImpl(LangModelContext context, ModuleElement.RequiresDirective requiresDirective)
   {
      super(context);
      this.requiresDirective = requiresDirective;
   }

   @Override
   public boolean isStatic()
   {
      return requiresDirective.isStatic();
   }

   @Override
   public boolean isTransitive()
   {
      return requiresDirective.isTransitive();
   }

   @Override
   public Module getDependency()
   {
      return LangModelAdapter.generalize(getApi(), requiresDirective.getDependency());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Requires otherRequires))
      {
         return false;
      }
      return Objects.equals(isStatic(), otherRequires.isStatic()) &&
             Objects.equals(isTransitive(), otherRequires.isTransitive()) &&
             Objects.equals(getDependency(), otherRequires.getDependency());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getDependency(), isStatic(), isTransitive());
   }

   @Override
   public String toString()
   {
      return requiresDirective.toString();
   }
}
