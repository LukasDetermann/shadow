package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.shadow.structure.Executable;

abstract class ExecutableTest<EXECUTABLE extends Executable>
{
   abstract void testGetParameters();

   abstract void testGetReturnType();

   abstract void testGetParameterTypes();

   abstract void testGetThrows();

   abstract void testIsVarArgs();

   abstract void testGetSurrounding();

   abstract void testGetPackage();

   abstract void testGetReceiverType();
}
