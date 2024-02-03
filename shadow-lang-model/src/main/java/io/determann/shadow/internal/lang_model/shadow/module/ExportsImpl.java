package io.determann.shadow.internal.lang_model.shadow.module;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.module.DirectiveKind;
import io.determann.shadow.api.shadow.module.Exports;

import javax.lang.model.element.ModuleElement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ExportsImpl extends DirectiveImpl implements Exports
{

   private final ModuleElement.ExportsDirective exportsDirective;

   public ExportsImpl(LangModelContext context, ModuleElement.ExportsDirective exportsDirective)
   {
      super(context);
      this.exportsDirective = exportsDirective;
   }

   @Override
   public Package getPackage()
   {
      return LangModelAdapter.getShadow(getApi(), exportsDirective.getPackage());
   }

   @Override
   public List<Module> getTargetModules()
   {
      return exportsDirective.getTargetModules() == null ?
             Collections.emptyList() :
             exportsDirective.getTargetModules()
                             .stream()
                             .map(moduleElement -> LangModelAdapter.<Module>getShadow(getApi(), moduleElement))
                             .toList();
   }

   @Override
   public boolean toAll()
   {
      return getTargetModules().isEmpty();
   }

   @Override
   public DirectiveKind getKind()
   {
      return DirectiveKind.EXPORTS;
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Exports otherExports))
      {
         return false;
      }
      return Objects.equals(getKind(), otherExports.getKind()) &&
             Objects.equals(getTargetModules(), otherExports.getTargetModules()) &&
             Objects.equals(getPackage(), otherExports.getPackage());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getKind(), getPackage(), getTargetModules());
   }

   @Override
   public String toString()
   {
      return exportsDirective.toString();
   }
}
