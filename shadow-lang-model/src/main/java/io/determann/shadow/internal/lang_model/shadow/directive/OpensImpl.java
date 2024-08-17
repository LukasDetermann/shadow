package io.determann.shadow.internal.lang_model.shadow.directive;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.directive.OpensLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.ModuleLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.PackageLangModel;
import io.determann.shadow.implementation.support.api.shadow.directive.OpensSupport;

import javax.lang.model.element.ModuleElement;
import java.util.List;

import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;

public class OpensImpl extends DirectiveImpl implements OpensLangModel
{
   private final ModuleElement.OpensDirective opensDirective;

   public OpensImpl(LangModelContext langModelContext, ModuleElement.OpensDirective opensDirective)
   {
      super(langModelContext);
      this.opensDirective = opensDirective;
   }

   @Override
   public PackageLangModel getPackage()
   {
      return LangModelAdapter.generalizePackage(getApi(), opensDirective.getPackage());
   }

   @Override
   public List<ModuleLangModel> getTargetModules()
   {
      return opensDirective.getTargetModules()
                           .stream()
                           .map(moduleElement -> LangModelAdapter.<ModuleLangModel>generalize(getApi(), moduleElement))
                           .toList();
   }

   @Override
   public boolean toAll()
   {
      return getTargetModules().isEmpty();
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
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
