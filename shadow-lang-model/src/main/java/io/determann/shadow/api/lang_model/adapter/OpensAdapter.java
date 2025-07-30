package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.internal.lang_model.shadow.directive.OpensImpl;

import javax.lang.model.element.ModuleElement;

public class OpensAdapter
{
   private final LM.Opens opens;

   OpensAdapter(LM.Opens opens)
   {
      this.opens = opens;
   }

   public ModuleElement.OpensDirective toOpensDirective()
   {
      return ((OpensImpl) opens).getMirror();
   }
}
