package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.structure.LM_Executable;
import io.determann.shadow.internal.lang_model.shadow.structure.ExecutableImpl;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;

public class LM_ExecutableAdapter
{
   private final LM_Executable executable;

   LM_ExecutableAdapter(LM_Executable executable)
   {
      this.executable = executable;
   }

   public ExecutableType toExecutableType()
   {
      return ((ExecutableImpl) executable).getMirror();
   }

   public ExecutableElement toExecutableElement()
   {
      return ((ExecutableImpl) executable).getElement();
   }
}
