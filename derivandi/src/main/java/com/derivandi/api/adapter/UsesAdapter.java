package com.derivandi.api.adapter;

import com.derivandi.api.Ap;
import com.derivandi.internal.shadow.directive.UsesImpl;

import javax.lang.model.element.ModuleElement;

public class UsesAdapter
{
   private final Ap.Uses uses;

   UsesAdapter(Ap.Uses uses)
   {
      this.uses = uses;
   }

   public ModuleElement.UsesDirective toUsesDirective()
   {
      return ((UsesImpl) uses).getMirror();
   }
}
