package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.reflection.R;

import java.lang.reflect.Executable;

public class ConstructorImpl
      extends ExecutableImpl
      implements R.Constructor
{
   public ConstructorImpl(Executable executable)
   {
      super(executable);
   }
}
