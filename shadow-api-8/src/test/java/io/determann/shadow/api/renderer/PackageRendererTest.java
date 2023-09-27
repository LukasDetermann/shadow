package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.ShadowApi.render;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PackageRendererTest
{
   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals("package java.lang;\n",
                                               render(shadowApi.getPackageOrThrow( "java.lang")).declaration()))
                   .compile();
   }
}