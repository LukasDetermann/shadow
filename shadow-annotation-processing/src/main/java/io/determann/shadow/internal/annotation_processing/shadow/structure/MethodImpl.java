package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;

import javax.lang.model.element.ExecutableElement;

public class MethodImpl extends ExecutableImpl implements Ap.Method
{
   public MethodImpl(Ap.Context context, ExecutableElement executableElement)
   {
      super(context, executableElement);
   }
}
