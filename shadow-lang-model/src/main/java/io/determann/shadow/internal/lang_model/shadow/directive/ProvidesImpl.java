package io.determann.shadow.internal.lang_model.shadow.directive;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.implementation.support.api.shadow.directive.ProvidesSupport;

import javax.lang.model.element.ModuleElement;
import java.util.List;

public class ProvidesImpl extends DirectiveImpl implements LM.Provides
{
   private final ModuleElement.ProvidesDirective providesDirective;

   public ProvidesImpl(LM.Context context, ModuleElement.ProvidesDirective providesDirective)
   {
      super(context);
      this.providesDirective = providesDirective;
   }

   @Override
   public LM.Declared getService()
   {
      return Adapters.adapt(getApi(), providesDirective.getService());
   }

   @Override
   public List<LM.Declared> getImplementations()
   {
      return providesDirective.getImplementations()
                              .stream()
                              .map(typeElement -> Adapters.<LM.Declared>adapt(getApi(), typeElement))
                              .toList();
   }

   @Override
   public Implementation getImplementation()
   {
      return getApi().getImplementation();
   }

   @Override
   public boolean equals(Object other)
   {
      return ProvidesSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return ProvidesSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return ProvidesSupport.toString(this);
   }
}
