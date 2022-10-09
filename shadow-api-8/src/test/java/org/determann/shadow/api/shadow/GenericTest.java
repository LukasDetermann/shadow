package org.determann.shadow.api.shadow;

import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericTest extends ShadowTest<Generic>
{
   GenericTest()
   {
      super(shadowApi -> shadowApi.getInterface("java.lang.Comparable").getFormalGenerics().get(0));
   }

   @Test
   void testGetExtends()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getClass("java.lang.Number"),
                                                        shadowApi.convert(shadowApi.getClass("GenericsExample")
                                                                                   .getGenerics()
                                                                                   .get(0))
                                                                 .toGeneric()
                                                                 .getExtends()))
                     .withCodeToCompile("GenericsExample.java", "                           import java.util.List;\n" +
                                                                "\n" +
                                                                "                           public class GenericsExample<T extends Number>\n" +
                                                                "                           {\n" +
                                                                "                              public static void foo(List<? super Number> a){ }\n" +
                                                                "                           }")
                     .compile();
   }

   @Test
   void testGetEnclosing()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getClass("GenericsExample"),
                                                        shadowApi.convert(shadowApi.getClass("GenericsExample")
                                                                                   .getGenerics()
                                                                                   .get(0))
                                                                 .toGeneric()
                                                                 .getEnclosing()))
                     .withCodeToCompile("GenericsExample.java", "                           import java.util.List;\n" +
                                                                "\n" +
                                                                "                           public class GenericsExample<T extends Number>\n" +
                                                                "                           {\n" +
                                                                "                              public static void foo(List<? super Number> a){ }\n" +
                                                                "                           }")
                     .compile();
   }
}
