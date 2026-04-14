package com.derivandi.api.adapter;

import com.derivandi.api.Ap;
import com.derivandi.internal.shadow.directive.ProvidesImpl;

import javax.lang.model.element.ModuleElement;

public class ProvidesAdapter
{
   private final Ap.Provides provides;

   ProvidesAdapter(Ap.Provides provides) {this.provides = provides;}

   public ModuleElement.ProvidesDirective toProvidesDirective()
   {
      return ((ProvidesImpl) provides).getMirror();
   }
}
