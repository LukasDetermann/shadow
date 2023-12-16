package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayRendererTest
{
   @Test
   void type()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Class string = shadowApi.getClassOrThrow("java.lang.String");
                               assertEquals("String[]", render(RenderingContext.DEFAULT, shadowApi.asArray(string)).type());
                               assertEquals("String[][]", render(RenderingContext.DEFAULT, shadowApi.asArray(shadowApi.asArray(string))).type());
                            })
                   .compile();
   }

   @Test
   void initialisation()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Class string = shadowApi.getClassOrThrow("java.lang.String");
                               assertEquals("new String[3][1]", render(RenderingContext.DEFAULT, shadowApi.asArray(string)).initialisation(3, 1));
                               assertEquals("new String[3][1]", render(RenderingContext.DEFAULT, shadowApi.asArray(string)).initialisation(3, 1));
                            })
                   .compile();
   }
}