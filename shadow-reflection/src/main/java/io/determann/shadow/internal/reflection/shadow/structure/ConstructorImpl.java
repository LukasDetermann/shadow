package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.structure.R_Constructor;

import java.lang.reflect.Executable;

public class ConstructorImpl
      extends ExecutableImpl
      implements R_Constructor
{
   public ConstructorImpl(Executable executable)
   {
      super(executable);
   }
}
