package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.api.shadow.Operations.DECLARED_GET_FIELD;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.compileTime(context -> context.getClassOrThrow("java.lang.String"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("java.lang.String")))
                     .test(aClass -> assertEquals("@jdk.internal.vm.annotation.Stable\nprivate final byte value;\n",
                                                  render(DEFAULT, requestOrThrow(aClass, DECLARED_GET_FIELD, "value"))
                                                        .declaration()));
   }
}