package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Constructor;

import javax.lang.model.element.ExecutableElement;

public class ConstructorImpl extends ExecutableImpl implements LM_Constructor
{
   public ConstructorImpl(LM_Context context, ExecutableElement executableElement)
   {
      super(context, executableElement);
   }
}
