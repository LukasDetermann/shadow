package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;

class PrimitiveRendererTest
{
   @Test
   void type()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Assertions.assertEquals("boolean", render(DEFAULT, shadowApi.getConstants().getPrimitiveBoolean()).type());
                               Assertions.assertEquals("byte", render(DEFAULT, shadowApi.getConstants().getPrimitiveByte()).type());
                               Assertions.assertEquals("short", render(DEFAULT, shadowApi.getConstants().getPrimitiveShort()).type());
                               Assertions.assertEquals("int", render(DEFAULT, shadowApi.getConstants().getPrimitiveInt()).type());
                               Assertions.assertEquals("long", render(DEFAULT, shadowApi.getConstants().getPrimitiveLong()).type());
                               Assertions.assertEquals("char", render(DEFAULT, shadowApi.getConstants().getPrimitiveChar()).type());
                               Assertions.assertEquals("float", render(DEFAULT, shadowApi.getConstants().getPrimitiveFloat()).type());
                               Assertions.assertEquals("double", render(DEFAULT, shadowApi.getConstants().getPrimitiveDouble()).type());
                            })
                   .compile();
   }
}