package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.internal.lang_model.shadow.structure.ExecutableImpl;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;

public class ExecutableAdapter
{
   private final LM.Executable executable;

   ExecutableAdapter(LM.Executable executable)
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
