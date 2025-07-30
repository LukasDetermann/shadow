package io.determann.shadow.internal.lang_model.shadow.directive;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.implementation.support.api.shadow.directive.UsesSupport;

import javax.lang.model.element.ModuleElement;

public class UsesImpl extends DirectiveImpl implements LM.Uses
{
   private final ModuleElement.UsesDirective usesDirective;

   public UsesImpl(LM.Context context, ModuleElement.UsesDirective usesDirective)
   {
      super(context);
      this.usesDirective = usesDirective;
   }

   @Override
   public LM.Declared getService()
   {
      return Adapters.adapt(getApi(), usesDirective.getService());
   }

   public ModuleElement.UsesDirective getMirror()
   {
      return usesDirective;
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
