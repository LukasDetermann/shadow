package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.converter.Converter.convert;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericTest extends ShadowTest<Generic>
{
   GenericTest()
   {
      super(shadowApi -> shadowApi.getInterfaceOrThrow("java.lang.Comparable").getGenerics().get(0));
   }

   @Test
   void testGetExtends()
   {
      ProcessorTest.process(shadowApi -> assertEquals(shadowApi.getClassOrThrow("java.lang.Number"),
                                                      convert(shadowApi.getClassOrThrow("GenericsExample")
                                                                       .getGenericTypes()
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
                                                                       .getGenericTypes()
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
}
