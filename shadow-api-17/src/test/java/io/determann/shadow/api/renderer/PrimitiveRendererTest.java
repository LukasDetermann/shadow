package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.ShadowApi.render;

class PrimitiveRendererTest
{
   @Test
   void type()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Assertions.assertEquals("boolean", render(shadowApi.getConstants().getPrimitiveBoolean()).type());
                               Assertions.assertEquals("byte", render(shadowApi.getConstants().getPrimitiveByte()).type());
                               Assertions.assertEquals("short", render(shadowApi.getConstants().getPrimitiveShort()).type());
                               Assertions.assertEquals("int", render(shadowApi.getConstants().getPrimitiveInt()).type());
                               Assertions.assertEquals("long", render(shadowApi.getConstants().getPrimitiveLong()).type());
                               Assertions.assertEquals("char", render(shadowApi.getConstants().getPrimitiveChar()).type());
                               Assertions.assertEquals("float", render(shadowApi.getConstants().getPrimitiveFloat()).type());
                               Assertions.assertEquals("double", render(shadowApi.getConstants().getPrimitiveDouble()).type());
                            })
                   .compile();
   }
}