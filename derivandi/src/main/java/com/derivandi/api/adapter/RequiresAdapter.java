package com.derivandi.api.adapter;

import com.derivandi.api.Ap;
import com.derivandi.internal.shadow.directive.RequiresImpl;

import javax.lang.model.element.ModuleElement;

public class RequiresAdapter
{
   private final Ap.Requires requires;

   RequiresAdapter(Ap.Requires requires)
   {
      this.requires = requires;
   }

   public ModuleElement.RequiresDirective toRequiresDirective()
   {
      return ((RequiresImpl) requires).getMirror();
   }
}
