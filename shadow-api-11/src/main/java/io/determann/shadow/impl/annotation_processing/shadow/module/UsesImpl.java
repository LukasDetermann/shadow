package io.determann.shadow.impl.annotation_processing.shadow.module;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.annotation_processing.MirrorAdapter;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.module.DirectiveKind;
import io.determann.shadow.api.shadow.module.Uses;

import javax.lang.model.element.ModuleElement;
import java.util.Objects;

public class UsesImpl extends DirectiveImpl implements Uses
{
   private final ModuleElement.UsesDirective usesDirective;

   public UsesImpl(AnnotationProcessingContext annotationProcessingContext, ModuleElement.UsesDirective usesDirective)
   {
      super(annotationProcessingContext);
      this.usesDirective = usesDirective;
   }

   @Override
   public Declared getService()
   {
      return MirrorAdapter.getShadow(getApi(), usesDirective.getService());
   }

   @Override
   public DirectiveKind getKind()
   {
      return DirectiveKind.USES;
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Uses))
      {
         return false;
      }
      Uses otherUses = (Uses) other;
      return Objects.equals(getKind(), otherUses.getKind()) &&
             Objects.equals(getService(), otherUses.getService());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getKind(), getService());
   }

   @Override
   public String toString()
   {
      return usesDirective.toString();
   }
}
