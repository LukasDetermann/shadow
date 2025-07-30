package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.internal.lang_model.shadow.directive.RequiresImpl;

import javax.lang.model.element.ModuleElement;

public class RequiresAdapter
{
   private final LM.Requires requires;

   RequiresAdapter(LM.Requires requires)
   {
      this.requires = requires;
   }

   public ModuleElement.RequiresDirective toRequiresDirective()
   {
      return ((RequiresImpl) requires).getMirror();
   }
}
