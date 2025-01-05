package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.directive.LM_Requires;
import io.determann.shadow.internal.lang_model.shadow.directive.RequiresImpl;

import javax.lang.model.element.ModuleElement;

public class LM_RequiresAdapter
{
   private final LM_Requires requires;

   LM_RequiresAdapter(LM_Requires requires)
   {
      this.requires = requires;
   }

   public ModuleElement.RequiresDirective toRequiresDirective()
   {
      return ((RequiresImpl) requires).getMirror();
   }
}
