package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.internal.annotation_processing.shadow.structure.ExecutableImpl;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;

public class ExecutableAdapter
{
   private final Ap.Executable executable;

   ExecutableAdapter(Ap.Executable executable)
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
