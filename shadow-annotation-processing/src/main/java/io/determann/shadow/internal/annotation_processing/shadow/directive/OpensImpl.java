package io.determann.shadow.internal.annotation_processing.shadow.directive;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.implementation.support.api.shadow.directive.OpensSupport;

import javax.lang.model.element.ModuleElement;
import java.util.List;

public class OpensImpl extends DirectiveImpl implements AP.Opens
{
   private final ModuleElement.OpensDirective opensDirective;

   public OpensImpl(AP.Context langModelContext, ModuleElement.OpensDirective opensDirective)
   {
      super(langModelContext);
      this.opensDirective = opensDirective;
   }

   @Override
   public AP.Package getPackage()
   {
      return Adapters.adapt(getApi(), opensDirective.getPackage());
   }

   @Override
   public List<AP.Module> getTargetModules()
   {
      return opensDirective.getTargetModules()
                           .stream()
                           .map(moduleElement -> Adapters.adapt(getApi(), moduleElement))
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
