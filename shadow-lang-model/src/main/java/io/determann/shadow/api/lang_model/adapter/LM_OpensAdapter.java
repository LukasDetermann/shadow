package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.directive.LM_Opens;
import io.determann.shadow.internal.lang_model.shadow.directive.OpensImpl;

import javax.lang.model.element.ModuleElement;

public class LM_OpensAdapter
{
   private final LM_Opens opens;

   LM_OpensAdapter(LM_Opens opens)
   {
      this.opens = opens;
   }

   public ModuleElement.OpensDirective toOpensDirective()
   {
      return ((OpensImpl) opens).getMirror();
   }
}
