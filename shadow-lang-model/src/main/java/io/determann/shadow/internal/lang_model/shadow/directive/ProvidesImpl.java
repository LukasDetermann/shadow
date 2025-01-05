package io.determann.shadow.internal.lang_model.shadow.directive;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.adapter.LM_Adapters;
import io.determann.shadow.api.lang_model.shadow.directive.LM_Provides;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.implementation.support.api.shadow.directive.ProvidesSupport;

import javax.lang.model.element.ModuleElement;
import java.util.List;

public class ProvidesImpl extends DirectiveImpl implements LM_Provides
{
   private final ModuleElement.ProvidesDirective providesDirective;

   public ProvidesImpl(LM_Context context, ModuleElement.ProvidesDirective providesDirective)
   {
      super(context);
      this.providesDirective = providesDirective;
   }

   @Override
   public LM_Declared getService()
   {
      return LM_Adapters.adapt(getApi(), providesDirective.getService());
   }

   @Override
   public List<LM_Declared> getImplementations()
   {
      return providesDirective.getImplementations()
                              .stream()
                              .map(typeElement -> LM_Adapters.<LM_Declared>adapt(getApi(), typeElement))
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
