package io.determann.shadow.internal.lang_model.shadow.directive;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.directive.ExportsLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.ModuleLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.PackageLangModel;
import io.determann.shadow.implementation.support.api.shadow.directive.ExportsSupport;

import javax.lang.model.element.ModuleElement;
import java.util.Collections;
import java.util.List;

import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;

public class ExportsImpl extends DirectiveImpl implements ExportsLangModel
{

   private final ModuleElement.ExportsDirective exportsDirective;

   public ExportsImpl(LangModelContext context, ModuleElement.ExportsDirective exportsDirective)
   {
      super(context);
      this.exportsDirective = exportsDirective;
   }

   @Override
   public PackageLangModel getPackage()
   {
      return LangModelAdapter.generalizePackage(getApi(), exportsDirective.getPackage());
   }

   @Override
   public List<ModuleLangModel> getTargetModules()
   {
      return exportsDirective.getTargetModules() == null ?
             Collections.emptyList() :
             exportsDirective.getTargetModules()
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
      return ExportsSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return ExportsSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return ExportsSupport.toString(this);
   }
}
