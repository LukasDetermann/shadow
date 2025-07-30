package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.LM;

import javax.lang.model.element.ExecutableElement;

public class ConstructorImpl extends ExecutableImpl implements LM.Constructor
{
   public ConstructorImpl(LM.Context context, ExecutableElement executableElement)
   {
      super(context, executableElement);
   }
}
