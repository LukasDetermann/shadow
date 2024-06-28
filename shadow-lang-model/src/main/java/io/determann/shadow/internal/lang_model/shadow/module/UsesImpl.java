package io.determann.shadow.internal.lang_model.shadow.module;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.module.DirectiveKind;
import io.determann.shadow.api.shadow.module.Uses;
import io.determann.shadow.api.shadow.type.Declared;

import javax.lang.model.element.ModuleElement;
import java.util.Objects;

public class UsesImpl extends DirectiveImpl implements Uses
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
      if (!(other instanceof Uses otherUses))
      {
         return false;
      }
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
