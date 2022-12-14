package org.determann.shadow.impl.shadow.module;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.shadow.Module;
import org.determann.shadow.api.shadow.Package;
import org.determann.shadow.api.shadow.module.DirectiveKind;
import org.determann.shadow.api.shadow.module.Exports;

import javax.lang.model.element.ModuleElement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ExportsImpl extends DirectiveImpl implements Exports
{

   private final ModuleElement.ExportsDirective exportsDirective;

   public ExportsImpl(ShadowApi shadowApi, ModuleElement.ExportsDirective exportsDirective)
   {
      super(shadowApi);
      this.exportsDirective = exportsDirective;
   }

   @Override
   public Package getPackage()
   {
      return getApi().getShadowFactory().shadowFromElement(exportsDirective.getPackage());
   }

   @Override
   public List<Module> getTargetModules()
   {
      return exportsDirective.getTargetModules() == null ?
             Collections.emptyList() :
             exportsDirective.getTargetModules()
                             .stream()
                             .map(moduleElement -> getApi().getShadowFactory().<Module>shadowFromElement(moduleElement))
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
      if (other == null || !getClass().equals(other.getClass()))
      {
         return false;
      }
      ExportsImpl otherExports = (ExportsImpl) other;
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
