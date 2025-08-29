package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.internal.annotation_processing.shadow.directive.OpensImpl;

import javax.lang.model.element.ModuleElement;

public class OpensAdapter
{
   private final Ap.Opens opens;

   OpensAdapter(Ap.Opens opens)
   {
      this.opens = opens;
   }

   public ModuleElement.OpensDirective toOpensDirective()
   {
      return ((OpensImpl) opens).getMirror();
   }
}
