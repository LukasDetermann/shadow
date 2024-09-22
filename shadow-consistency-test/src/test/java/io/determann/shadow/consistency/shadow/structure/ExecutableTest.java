package io.determann.shadow.consistency.shadow.structure;

import io.determann.shadow.api.shadow.structure.C_Executable;

abstract class ExecutableTest<EXECUTABLE extends C_Executable>
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
