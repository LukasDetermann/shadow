package org.determann.shadow.api.shadow;

import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import static org.determann.shadow.api.ShadowApi.convert;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericTest extends ShadowTest<Generic>
{
   GenericTest()
   {
      super(shadowApi -> shadowApi.getInterfaceOrThrow("java.lang.Comparable").getFormalGenerics().get(0));
   }

   @Test
   void testGetExtends()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getClassOrThrow("java.lang.Number"),
                                                        convert(shadowApi.getClassOrThrow("GenericsExample")
                                                                                   .getGenerics()
                                                                                   .get(0))
                                                                 .toGeneric()
                                                                 .getExtends()))
                     .withCodeToCompile("GenericsExample.java", """
                           import java.util.List;

                           public class GenericsExample<T extends Number>
                           {
                              public static void foo(List<? super Number> a){ }
                           }""")
                     .compile();
   }

   @Test
   void testGetEnclosing()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getClassOrThrow("GenericsExample"),
                                                        convert(shadowApi.getClassOrThrow("GenericsExample")
                                                                                   .getGenerics()
                                                                                   .get(0))
                                                                 .toGeneric()
                                                                 .getEnclosing()))
                     .withCodeToCompile("GenericsExample.java", """
                           import java.util.List;

                           public class GenericsExample<T extends Number>
                           {
                              public static void foo(List<? super Number> a){ }
                           }""")
                     .compile();
   }

   @Test
   void testGetPackage()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getPackagesOrThrow("org.determann.shadow.example.processed.test.generics").get(0),
                                                        convert(shadowApi.getClassOrThrow(
                                                                                         "org.determann.shadow.example.processed.test.generics.GenericsExample")
                                                                                   .getGenerics()
                                                                                   .get(0))
                                                                 .toGeneric()
                                                                 .getPackage()))
                     .withCodeToCompile("GenericsExample.java", """
                           package org.determann.shadow.example.processed.test.generics;

                           import java.util.List;

                           public class GenericsExample<T extends Number>
                           {
                              public static void foo(List<? super Number> a) {}
                           }
                           """)
                     .compile();
   }
}
