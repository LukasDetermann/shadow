package io.determann.shadow.api.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.consistency.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.meta_meta.Operations.DECLARED_GET_FIELD;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;
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