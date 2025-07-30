package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.internal.lang_model.shadow.directive.ExportsImpl;

import javax.lang.model.element.ModuleElement;

public class ExportsAdapter
{
   private final LM.Exports exports;

   ExportsAdapter(LM.Exports exports)
   {
      this.exports = exports;
   }

   public ModuleElement.ExportsDirective toExportsDirective()
   {
      return ((ExportsImpl) exports).getMirror();
   }
}
