package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import io.determann.shadow.tck.internal.RenderingTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.CLASS_AS_UNBOXED;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.RenderingTestBuilder.renderingTest;

class PrimitiveRendererTest
{

   private RenderingTestBuilder<C_Class> renderingTest;

   @BeforeEach
   void setup()
   {
      renderingTest = renderingTest(C_Class.class).withRender(cClass ->
                                                              {
                                                                 C_Primitive result = requestOrThrow(cClass, CLASS_AS_UNBOXED);
                                                                 return render(DEFAULT, result).type();
                                                              });
   }

   @Test
   void type()
   {
      renderingTest.withToRender("java.lang.Boolean").withExpected("boolean").test();

      renderingTest.withToRender("java.lang.Byte").withExpected("byte").test();

      renderingTest.withToRender("java.lang.Short").withExpected("short").test();

      renderingTest.withToRender("java.lang.Integer").withExpected("int").test();

      renderingTest.withToRender("java.lang.Long").withExpected("long").test();

      renderingTest.withToRender("java.lang.Character").withExpected("char").test();

      renderingTest.withToRender("java.lang.Float").withExpected("float").test();

      renderingTest.withToRender("java.lang.Double").withExpected("double").test();
   }
}