package io.determann.shadow.internal.lang_model.shadow.directive;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.directive.ProvidesLangModel;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.implementation.support.api.shadow.directive.ProvidesSupport;

import javax.lang.model.element.ModuleElement;
import java.util.List;

import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;

public class ProvidesImpl extends DirectiveImpl implements ProvidesLangModel
{
   private final ModuleElement.ProvidesDirective providesDirective;

   public ProvidesImpl(LangModelContext context, ModuleElement.ProvidesDirective providesDirective)
   {
      super(context);
      this.providesDirective = providesDirective;
   }

   @Override
   public Declared getService()
   {
      return LangModelAdapter.generalize(getApi(), providesDirective.getService());
   }

   @Override
   public List<Declared> getImplementations()
   {
      return providesDirective.getImplementations()
                              .stream()
                              .map(typeElement -> LangModelAdapter.<Declared>generalize(getApi(), typeElement))
                              .toList();
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
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
