package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldRendererTest
{

   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals("@jdk.internal.vm.annotation.Stable\nprivate final byte value;\n",
                                               render(DEFAULT, shadowApi.getClassOrThrow("java.lang.String").getFieldOrThrow("value"))
                                                     .declaration()))
                   .compile();
   }
}