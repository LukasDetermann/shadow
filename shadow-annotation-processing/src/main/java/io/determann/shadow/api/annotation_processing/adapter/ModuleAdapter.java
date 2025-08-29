package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.internal.annotation_processing.shadow.structure.ModuleImpl;

import javax.lang.model.element.ModuleElement;
import javax.lang.model.type.NoType;

public class ModuleAdapter
{
   private final Ap.Module module;

   ModuleAdapter(Ap.Module module)
   {
      this.module = module;
   }

   public NoType toNoType()
   {
      return ((ModuleImpl) module).getMirror();
   }

   public ModuleElement toModuleElement()
   {
      return ((ModuleImpl) module).getElement();
   }
}
