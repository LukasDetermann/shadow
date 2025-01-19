package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.structure.R_Method;

import java.lang.reflect.Executable;

public class MethodImpl
      extends ExecutableImpl
      implements R_Method
{
   public MethodImpl(Executable executable)
   {
      super(executable);
   }
}
