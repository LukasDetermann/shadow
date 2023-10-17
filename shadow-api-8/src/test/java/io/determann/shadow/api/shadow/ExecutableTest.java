package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;

import java.util.function.Function;

abstract class ExecutableTest<EXECUTABLE extends Executable> extends ShadowTest<EXECUTABLE>
{
   ExecutableTest(Function<AnnotationProcessingContext, EXECUTABLE> executableSupplier)
   {
      super(executableSupplier);
   }

   abstract void testGetParameters();

   abstract void testGetReturnType();

   abstract void testGetParameterTypes();

   abstract void testGetThrows();

   abstract void testIsVarArgs();

   abstract void testGetSurrounding();

   abstract void testGetReceiverType();
}
