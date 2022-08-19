package org.determann.shadow.impl.shadow.module;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.shadow.Module;
import org.determann.shadow.api.shadow.Package;
import org.determann.shadow.api.shadow.module.DirectiveKind;
import org.determann.shadow.api.shadow.module.Opens;

import javax.lang.model.element.ModuleElement;
import java.util.List;
import java.util.Objects;

public class OpensImpl extends DirectiveImpl implements Opens
{
   private final ModuleElement.OpensDirective opensDirective;

   public OpensImpl(ShadowApi shadowApi, ModuleElement.OpensDirective opensDirective)
   {
      super(shadowApi);
      this.opensDirective = opensDirective;
   }

   @Override
   public Package getPackage()
   {
      return shadowApi.getShadowFactory().shadowFromElement(opensDirective.getPackage());
   }

   @Override
   public List<Module> getTargetModules()
   {
      return opensDirective.getTargetModules()
                           .stream()
                           .map(moduleElement -> shadowApi.getShadowFactory().<Module>shadowFromElement(moduleElement))
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
      return DirectiveKind.OPENS;
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
      OpensImpl otherOpens = (OpensImpl) other;
      return Objects.equals(getKind(), otherOpens.getKind()) &&
             Objects.equals(getTargetModules(), otherOpens.getTargetModules()) &&
             Objects.equals(getPackage(), otherOpens.getPackage());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getKind(), getPackage(), getTargetModules());
   }

   @Override
   public String toString()
   {
      return opensDirective.toString();
   }
}
