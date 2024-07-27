package io.determann.shadow.internal.lang_model.shadow.directive;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.directive.UsesLangModel;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.implementation.support.api.shadow.directive.UsesSupport;

import javax.lang.model.element.ModuleElement;

import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;

public class UsesImpl extends DirectiveImpl implements UsesLangModel
{
   private final ModuleElement.UsesDirective usesDirective;

   public UsesImpl(LangModelContext context, ModuleElement.UsesDirective usesDirective)
   {
      super(context);
      this.usesDirective = usesDirective;
   }

   @Override
   public Declared getService()
   {
      return LangModelAdapter.generalize(getApi(), usesDirective.getService());
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
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
