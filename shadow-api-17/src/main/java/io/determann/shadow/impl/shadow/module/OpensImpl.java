package io.determann.shadow.impl.shadow.module;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.module.DirectiveKind;
import io.determann.shadow.api.shadow.module.Opens;

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
      return getApi().getShadowFactory().shadowFromElement(opensDirective.getPackage());
   }

   @Override
   public List<Module> getTargetModules()
   {
      return opensDirective.getTargetModules()
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