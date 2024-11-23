package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.tck.internal.RenderingTestBuilder;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.DECLARED_GET_FIELD;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;

class FieldRendererTest
{
   @Test
   void declaration()
   {
      RenderingTestBuilder.renderingTest(C_Class.class)
            .withToRender("java.lang.String")
            .withRender(cClass ->
                        {
                           C_Field field = requestOrThrow(cClass, DECLARED_GET_FIELD, "value");
                           return render(DEFAULT, field).declaration();
                        })
            .withExpected("@jdk.internal.vm.annotation.Stable\nprivate final byte value;\n")
            .test();

   }
}