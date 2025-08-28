package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.AP;

import javax.lang.model.element.ExecutableElement;

public class MethodImpl extends ExecutableImpl implements AP.Method
{
   public MethodImpl(AP.Context context, ExecutableElement executableElement)
   {
      super(context, executableElement);
   }
}
