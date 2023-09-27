package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.ShadowApi.render;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayRendererTest
{
   @Test
   void type()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Class string = shadowApi.getClassOrThrow("java.lang.String");
                               assertEquals("String[]", render(string.asArray()).type());
                               assertEquals("String[][]", render(string.asArray().asArray()).type());
                            })
                   .compile();
   }

   @Test
   void initialisation()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Class string = shadowApi.getClassOrThrow("java.lang.String");
                               assertEquals("new String[3][1]", render(string.asArray()).initialisation(3, 1));
                               assertEquals("new String[3][1]", render(string.asArray().asArray()).initialisation(3, 1));
                            })
                   .compile();
   }
}