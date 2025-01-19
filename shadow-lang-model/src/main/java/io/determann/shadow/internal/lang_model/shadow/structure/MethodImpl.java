package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Method;

import javax.lang.model.element.ExecutableElement;

public class MethodImpl extends ExecutableImpl implements LM_Method
{
   public MethodImpl(LM_Context context, ExecutableElement executableElement)
   {
      super(context, executableElement);
   }
}
