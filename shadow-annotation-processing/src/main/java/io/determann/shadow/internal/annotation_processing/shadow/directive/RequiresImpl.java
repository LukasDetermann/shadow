package io.determann.shadow.internal.annotation_processing.shadow.directive;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import io.determann.shadow.api.annotation_processing.dsl.requires.RequiresNameStep;

import javax.lang.model.element.ModuleElement;
import java.util.Objects;

import static io.determann.shadow.api.annotation_processing.dsl.JavaDsl.requires;

public class RequiresImpl extends DirectiveImpl implements Ap.Requires
{
   private final ModuleElement.RequiresDirective requiresDirective;

   public RequiresImpl(Ap.Context context, ModuleElement.RequiresDirective requiresDirective)
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
   public Ap.Module getDependency()
   {
      return Adapters.adapt(getApi(), requiresDirective.getDependency());
   }

   public ModuleElement.RequiresDirective getMirror()
   {
      return requiresDirective;
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      RequiresNameStep nameStep;
      if (isStatic())
      {
         nameStep = requires().static_();
      }
      else if (isTransitive())
      {
         nameStep = requires().transitive();
      }
      else
      {
         nameStep = requires();
      }
      return nameStep.dependency(getDependency()).renderDeclaration(renderingContext);
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Ap.Requires requires &&
             Objects.equals(isStatic(), requires.isStatic()) &&
             Objects.equals(isTransitive(), requires.isTransitive()) &&
             Objects.equals(getDependency(), requires.getDependency());

   }

   @Override
   public int hashCode()
   {
      return Objects.hash(isStatic(), isTransitive(), getDependency());
   }

   @Override
   public String toString()
   {
      return "Requires{" +
             "static=" + isStatic() +
             ", transitive=" + isTransitive() +
             ", dependency=" + getDependency() +
             '}';

   }
}
