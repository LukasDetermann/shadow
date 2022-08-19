package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Executable;

import java.util.function.Supplier;

public abstract class ExecutableTest<EXECUTABLE extends Executable> extends ShadowTest<EXECUTABLE>
{
   protected ExecutableTest(Supplier<EXECUTABLE> executableSupplier)
   {
      super(executableSupplier);
   }

   abstract void testGetParameters();

   abstract void testGetReturnType();

   abstract void testGetParameterTypes();

   abstract void testGetThrows();

   abstract void testIsVarArgs();

   abstract void testGetSurrounding();

   abstract void testGetPackage();

   abstract void testGetReceiverType();
}
