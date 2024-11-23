package io.determann.shadow.internal.lang_model.shadow.directive;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.directive.LM_Uses;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.implementation.support.api.shadow.directive.UsesSupport;

import javax.lang.model.element.ModuleElement;

public class UsesImpl extends DirectiveImpl implements LM_Uses
{
   private final ModuleElement.UsesDirective usesDirective;

   public UsesImpl(LM_Context context, ModuleElement.UsesDirective usesDirective)
   {
      super(context);
      this.usesDirective = usesDirective;
   }

   @Override
   public LM_Declared getService()
   {
      return LM_Adapter.generalize(getApi(), usesDirective.getService());
   }

   @Override
   public Implementation getImplementation()
   {
      return getApi().getImplementation();
   }

   @Override
   public boolean equals(Object other)
   {
      return UsesSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return UsesSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return UsesSupport.toString(this);
   }
}
