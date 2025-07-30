package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.reflection.R;

import java.lang.reflect.Executable;

public class MethodImpl
      extends ExecutableImpl
      implements R.Method
{
   public MethodImpl(Executable executable)
   {
      super(executable);
   }
}
