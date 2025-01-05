package io.determann.shadow.internal.lang_model.shadow.directive;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.adapter.LM_Adapters;
import io.determann.shadow.api.lang_model.shadow.directive.LM_Opens;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Module;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Package;
import io.determann.shadow.implementation.support.api.shadow.directive.OpensSupport;

import javax.lang.model.element.ModuleElement;
import java.util.List;

public class OpensImpl extends DirectiveImpl implements LM_Opens
{
   private final ModuleElement.OpensDirective opensDirective;

   public OpensImpl(LM_Context langModelContext, ModuleElement.OpensDirective opensDirective)
   {
      super(langModelContext);
      this.opensDirective = opensDirective;
   }

   @Override
   public LM_Package getPackage()
   {
      return LM_Adapters.adapt(getApi(), opensDirective.getPackage());
   }

   @Override
   public List<LM_Module> getTargetModules()
   {
      return opensDirective.getTargetModules()
                           .stream()
                           .map(moduleElement -> LM_Adapters.adapt(getApi(), moduleElement))
                           .toList();
   }

   @Override
   public boolean toAll()
   {
      return getTargetModules().isEmpty();
   }

   public ModuleElement.OpensDirective getMirror()
   {
      return opensDirective;
   }

   @Override
   public Implementation getImplementation()
   {
      return getApi().getImplementation();
   }

   @Override
   public boolean equals(Object other)
   {
      return OpensSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return OpensSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return OpensSupport.toString(this);
   }
}
