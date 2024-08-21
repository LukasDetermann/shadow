package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.DECLARED_GET_FIELD;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.<C_Class>compileTime(context -> context.getClassOrThrow("java.lang.String"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("java.lang.String")))
                     .test(aClass -> assertEquals("@jdk.internal.vm.annotation.Stable\nprivate final byte value;\n",
                                                  render(DEFAULT, requestOrThrow(aClass, DECLARED_GET_FIELD, "value"))
                                                        .declaration()));
   }
}