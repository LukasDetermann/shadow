package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.internal.annotation_processing.shadow.directive.UsesImpl;

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
