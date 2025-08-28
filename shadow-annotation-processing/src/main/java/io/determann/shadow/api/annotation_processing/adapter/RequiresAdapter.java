package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.internal.annotation_processing.shadow.directive.RequiresImpl;

import javax.lang.model.element.ModuleElement;

public class RequiresAdapter
{
   private final AP.Requires requires;

   RequiresAdapter(AP.Requires requires)
   {
      this.requires = requires;
   }

   public ModuleElement.RequiresDirective toRequiresDirective()
   {
      return ((RequiresImpl) requires).getMirror();
   }
}
