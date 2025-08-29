package io.determann.shadow.internal.annotation_processing.shadow.directive;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.implementation.support.api.shadow.directive.UsesSupport;

import javax.lang.model.element.ModuleElement;

public class UsesImpl extends DirectiveImpl implements Ap.Uses
{
   private final ModuleElement.UsesDirective usesDirective;

   public UsesImpl(Ap.Context context, ModuleElement.UsesDirective usesDirective)
   {
      super(context);
      this.usesDirective = usesDirective;
   }

   @Override
   public Ap.Declared getService()
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
