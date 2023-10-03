package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.ShadowApi.render;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldRendererTest
{

   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals("private final char value;\n",
                                               render(shadowApi.getClassOrThrow("java.lang.String").getFieldOrThrow("value"))
                                                     .declaration()))
                   .compile();
   }
}