package io.determann.shadow.internal.lang_model.shadow.directive;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.adapter.LM_Adapters;
import io.determann.shadow.api.lang_model.shadow.directive.LM_Requires;
import io.determann.shadow.api.shadow.structure.C_Module;
import io.determann.shadow.implementation.support.api.shadow.directive.RequiresSupport;

import javax.lang.model.element.ModuleElement;

public class RequiresImpl extends DirectiveImpl implements LM_Requires
{
   private final ModuleElement.RequiresDirective requiresDirective;

   public RequiresImpl(LM_Context context, ModuleElement.RequiresDirective requiresDirective)
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
   public C_Module getDependency()
   {
      return LM_Adapters.adapt(getApi(), requiresDirective.getDependency());
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
