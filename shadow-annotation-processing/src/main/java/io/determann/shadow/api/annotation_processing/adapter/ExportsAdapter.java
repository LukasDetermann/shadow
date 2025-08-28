package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.internal.annotation_processing.shadow.directive.ExportsImpl;

import javax.lang.model.element.ModuleElement;

public class ExportsAdapter
{
   private final AP.Exports exports;

   ExportsAdapter(AP.Exports exports)
   {
      this.exports = exports;
   }

   public ModuleElement.ExportsDirective toExportsDirective()
   {
      return ((ExportsImpl) exports).getMirror();
   }
}
