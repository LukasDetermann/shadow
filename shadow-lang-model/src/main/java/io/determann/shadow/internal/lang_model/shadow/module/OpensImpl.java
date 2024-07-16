package io.determann.shadow.internal.lang_model.shadow.module;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.module.Opens;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;

import javax.lang.model.element.ModuleElement;
import java.util.List;
import java.util.Objects;

public class OpensImpl extends DirectiveImpl implements Opens
{
   private final ModuleElement.OpensDirective opensDirective;

   public OpensImpl(LangModelContext langModelContext, ModuleElement.OpensDirective opensDirective)
   {
      super(langModelContext);
      this.opensDirective = opensDirective;
   }

   @Override
   public Package getPackage()
   {
      return LangModelAdapter.generalizePackage(getApi(), opensDirective.getPackage());
   }

   @Override
   public List<Module> getTargetModules()
   {
      return opensDirective.getTargetModules()
                           .stream()
                           .map(moduleElement -> LangModelAdapter.<Module>generalize(getApi(), moduleElement))
                           .toList();
   }

   @Override
   public boolean toAll()
   {
      return getTargetModules().isEmpty();
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Opens otherOpens))
      {
         return false;
      }
      return Objects.equals(getTargetModules(), otherOpens.getTargetModules()) &&
             Objects.equals(getPackage(), otherOpens.getPackage());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getPackage(), getTargetModules());
   }

   @Override
   public String toString()
   {
      return opensDirective.toString();
   }
}
