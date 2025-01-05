package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.directive.LM_Exports;
import io.determann.shadow.internal.lang_model.shadow.directive.ExportsImpl;

import javax.lang.model.element.ModuleElement;

public class LM_ExportsAdapter
{
   private final LM_Exports exports;

   LM_ExportsAdapter(LM_Exports exports)
   {
      this.exports = exports;
   }

   public ModuleElement.ExportsDirective toExportsDirective()
   {
      return ((ExportsImpl) exports).getMirror();
   }
}
