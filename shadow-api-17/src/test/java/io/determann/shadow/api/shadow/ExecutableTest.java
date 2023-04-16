package io.determann.shadow.api.shadow;

import io.determann.shadow.api.ShadowApi;

import java.util.function.Function;

abstract class ExecutableTest<EXECUTABLE extends Executable> extends ShadowTest<EXECUTABLE>
{
   ExecutableTest(Function<ShadowApi, EXECUTABLE> executableSupplier)
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
