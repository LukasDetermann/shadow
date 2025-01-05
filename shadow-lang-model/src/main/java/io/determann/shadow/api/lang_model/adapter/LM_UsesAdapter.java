package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.directive.LM_Uses;
import io.determann.shadow.internal.lang_model.shadow.directive.UsesImpl;

import javax.lang.model.element.ModuleElement;

public class LM_UsesAdapter
{
   private final LM_Uses uses;

   LM_UsesAdapter(LM_Uses uses)
   {
      this.uses = uses;
   }

   public ModuleElement.UsesDirective toUsesDirective()
   {
      return ((UsesImpl) uses).getMirror();
   }
}
