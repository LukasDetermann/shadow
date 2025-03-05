package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.type.C_Class;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.DECLARED_GET_FIELD;
import static io.determann.shadow.api.Operations.GET_CLASS;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldRendererTest
{
   @Test
   void declaration()
   {
      test(implementation ->
           {
              C_Class cClass = requestOrThrow(implementation, GET_CLASS, "java.lang.String");
              C_Field field = requestOrThrow(cClass, DECLARED_GET_FIELD, "value");
              assertEquals("@jdk.internal.vm.annotation.Stable\nprivate final byte value;\n", render(field).declaration(DEFAULT));
           });
   }
}