package org.determann.shadow.api.shadow;

import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import static org.determann.shadow.api.ShadowApi.convert;
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
                                                        convert(shadowApi.getClass("GenericsExample")
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
                                                        convert(shadowApi.getClass("GenericsExample")
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

   @Test
   void testGetPackage()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getPackages("org.determann.shadow.example.processed.test.generics").get(0),
                                                        convert(shadowApi.getClass(
                                                                                         "org.determann.shadow.example.processed.test.generics.GenericsExample")
                                                                                   .getGenerics()
                                                                                   .get(0))
                                                                 .toGeneric()
                                                                 .getPackage()))
                     .withCodeToCompile("GenericsExample.java",
                                        "                           package org.determann.shadow.example.processed.test.generics;\n" +
                                        "\n" +
                                        "                           import java.util.List;\n" +
                                        "\n" +
                                        "                           public class GenericsExample<T extends Number>\n" +
                                        "                           {\n" +
                                        "                              public static void foo(List<? super Number> a) {}\n" +
                                        "                           }")
                     .compile();
   }
}
