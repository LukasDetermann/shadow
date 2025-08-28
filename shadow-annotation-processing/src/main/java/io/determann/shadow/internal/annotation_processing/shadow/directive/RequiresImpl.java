package io.determann.shadow.internal.annotation_processing.shadow.directive;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.implementation.support.api.shadow.directive.RequiresSupport;

import javax.lang.model.element.ModuleElement;

public class RequiresImpl extends DirectiveImpl implements AP.Requires
{
   private final ModuleElement.RequiresDirective requiresDirective;

   public RequiresImpl(AP.Context context, ModuleElement.RequiresDirective requiresDirective)
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
      return RequiresSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return RequiresSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return RequiresSupport.toString(this);
   }
}
