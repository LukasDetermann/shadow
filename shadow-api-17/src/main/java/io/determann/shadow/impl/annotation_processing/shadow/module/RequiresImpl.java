package io.determann.shadow.impl.annotation_processing.shadow.module;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.annotation_processing.MirrorAdapter;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.module.DirectiveKind;
import io.determann.shadow.api.shadow.module.Requires;

import javax.lang.model.element.ModuleElement;
import java.util.Objects;

public class RequiresImpl extends DirectiveImpl implements Requires
{
   private final ModuleElement.RequiresDirective requiresDirective;

   public RequiresImpl(AnnotationProcessingContext annotationProcessingContext, ModuleElement.RequiresDirective requiresDirective)
   {
      super(annotationProcessingContext);
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
      return MirrorAdapter.getShadow(getApi(), requiresDirective.getDependency());
   }

   @Override
   public DirectiveKind getKind()
   {
      return DirectiveKind.REQUIRES;
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
      return Objects.equals(getKind(), otherRequires.getKind()) &&
             Objects.equals(isStatic(), otherRequires.isStatic()) &&
             Objects.equals(isTransitive(), otherRequires.isTransitive()) &&
             Objects.equals(getDependency(), otherRequires.getDependency());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getKind(), getDependency(), isStatic(), isTransitive());
   }

   @Override
   public String toString()
   {
      return requiresDirective.toString();
   }
}
