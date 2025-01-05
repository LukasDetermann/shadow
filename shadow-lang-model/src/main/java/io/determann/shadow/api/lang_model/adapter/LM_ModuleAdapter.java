package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.structure.LM_Module;
import io.determann.shadow.internal.lang_model.shadow.structure.ModuleImpl;

import javax.lang.model.element.ModuleElement;
import javax.lang.model.type.NoType;

public class LM_ModuleAdapter
{
   private final LM_Module module;

   LM_ModuleAdapter(LM_Module module)
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
