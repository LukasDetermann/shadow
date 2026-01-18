package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.internal.annotation_processing.shadow.directive.ProvidesImpl;

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
