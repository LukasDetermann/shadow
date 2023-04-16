package io.determann.shadow.impl.shadow.module;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.module.DirectiveKind;
import io.determann.shadow.api.shadow.module.Uses;

import javax.lang.model.element.ModuleElement;
import java.util.Objects;

public class UsesImpl extends DirectiveImpl implements Uses
{
   private final ModuleElement.UsesDirective usesDirective;

   public UsesImpl(ShadowApi shadowApi, ModuleElement.UsesDirective usesDirective)
   {
      super(shadowApi);
      this.usesDirective = usesDirective;
   }

   @Override
   public Declared getService()
   {
      return getApi().getShadowFactory().shadowFromElement(usesDirective.getService());
   }

   @Override
   public DirectiveKind getKind()
   {
      return DirectiveKind.USES;
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
      UsesImpl otherUses = (UsesImpl) other;
      return Objects.equals(getKind(), otherUses.getKind()) &&
             Objects.equals(getService(), otherUses.getService());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getKind(), getService());
   }

   @Override
   public String toString()
   {
      return usesDirective.toString();
   }
}
