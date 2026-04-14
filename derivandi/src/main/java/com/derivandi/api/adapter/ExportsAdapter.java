package com.derivandi.api.adapter;

import com.derivandi.api.Ap;
import com.derivandi.internal.shadow.directive.ExportsImpl;

import javax.lang.model.element.ModuleElement;

public class ExportsAdapter
{
   private final Ap.Exports exports;

   ExportsAdapter(Ap.Exports exports)
   {
      this.exports = exports;
   }

   public ModuleElement.ExportsDirective toExportsDirective()
   {
      return ((ExportsImpl) exports).getMirror();
   }
}
