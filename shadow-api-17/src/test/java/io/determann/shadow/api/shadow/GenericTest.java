package io.determann.shadow.api.shadow;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.ShadowApi.convert;
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
      ProcessorTest.process(shadowApi -> assertEquals(shadowApi.getClassOrThrow("java.lang.Number"),
                                                      convert(shadowApi.getClassOrThrow("GenericsExample")
                                                                                   .getGenerics()
                                                                                   .get(0))
                                                                 .toGenericOrThrow()
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
      ProcessorTest.process(shadowApi -> assertEquals(shadowApi.getClassOrThrow("GenericsExample"),
                                                      convert(shadowApi.getClassOrThrow("GenericsExample")
                                                                                   .getGenerics()
                                                                                   .get(0))
                                                                 .toGenericOrThrow()
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
      ProcessorTest.process(shadowApi -> assertEquals(shadowApi.getPackagesOrThrow("io.determann.shadow.example.processed.test.generics").get(0),
                                                      convert(shadowApi.getClassOrThrow(
                                                                                         "io.determann.shadow.example.processed.test.generics.GenericsExample")
                                                                                   .getGenerics()
                                                                                   .get(0))
                                                                 .toGenericOrThrow()
                                                                 .getPackage()))
                   .withCodeToCompile("GenericsExample.java", """
                           package io.determann.shadow.example.processed.test.generics;

                           import java.util.List;

                           public class GenericsExample<T extends Number>
                           {
                              public static void foo(List<? super Number> a) {}
                           }
                           """)
                   .compile();
   }
}
