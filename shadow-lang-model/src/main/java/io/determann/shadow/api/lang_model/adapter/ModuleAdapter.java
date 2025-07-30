package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.internal.lang_model.shadow.structure.ModuleImpl;

import javax.lang.model.element.ModuleElement;
import javax.lang.model.type.NoType;

public class ModuleAdapter
{
   private final LM.Module module;

   ModuleAdapter(LM.Module module)
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
