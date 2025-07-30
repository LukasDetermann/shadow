package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.internal.lang_model.shadow.directive.UsesImpl;

import javax.lang.model.element.ModuleElement;

public class UsesAdapter
{
   private final LM.Uses uses;

   UsesAdapter(LM.Uses uses)
   {
      this.uses = uses;
   }

   public ModuleElement.UsesDirective toUsesDirective()
   {
      return ((UsesImpl) uses).getMirror();
   }
}
