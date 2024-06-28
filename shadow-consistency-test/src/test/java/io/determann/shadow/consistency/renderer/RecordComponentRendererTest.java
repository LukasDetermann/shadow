package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.api.shadow.Operations.RECORD_GET_RECORD_COMPONENT;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecordComponentRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.compileTime(context -> context.getRecordOrThrow("RecordComponentExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("RecordComponentExample")))
                     .withCode("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                     .test(aClass -> assertEquals("Long id",
                                                  render(DEFAULT, requestOrThrow(aClass, RECORD_GET_RECORD_COMPONENT, "id")).declaration()));
   }

   @Test
   void invocation()
   {
      ConsistencyTest.compileTime(context -> context.getRecordOrThrow("RecordComponentExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("RecordComponentExample")))
                     .withCode("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                     .test(aClass -> assertEquals("id()",
                                                  render(DEFAULT, requestOrThrow(aClass, RECORD_GET_RECORD_COMPONENT, "id")).invocation()));
   }
}