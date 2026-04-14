package com.derivandi.api.adapter;

import com.derivandi.api.Ap;
import com.derivandi.internal.shadow.directive.OpensImpl;

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
