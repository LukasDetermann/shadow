package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PackageRendererTest
{
   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals("package java.lang;\n",
                                               render(DEFAULT, shadowApi.getPackageOrThrow("java.lang")).declaration()))
                   .compile();
   }
}