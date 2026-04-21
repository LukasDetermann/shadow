package com.derivandi.api.adapter;

import com.derivandi.api.D;
import com.derivandi.internal.shadow.directive.ProvidesImpl;

import javax.lang.model.element.ModuleElement;

public class ProvidesAdapter
{
   private final D.Provides provides;

   ProvidesAdapter(D.Provides provides) {this.provides = provides;}

   public ModuleElement.ProvidesDirective toProvidesDirective()
   {
      return ((ProvidesImpl) provides).getMirror();
   }
}
