package io.determann.shadow.internal.annotation_processing.shadow.directive;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;

import javax.lang.model.element.ModuleElement;
import java.util.Objects;

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
   public C.Module getDependency()
   {
      return Adapters.adapt(getApi(), requiresDirective.getDependency());
   }

   public ModuleElement.RequiresDirective getMirror()
   {
      return requiresDirective;
   }

   @Override
   public Implementation getImplementation()
   {
      return getApi().getImplementation();
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
