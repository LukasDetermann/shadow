package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.type.LM_Generic;
import io.determann.shadow.api.shadow.type.C_Generic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericTest extends ShadowTest<C_Generic>
{
   GenericTest()
   {
      super(context -> context.getInterfaceOrThrow("java.lang.Comparable").getGenerics().get(0));
   }

   @Test
   void testGetExtends()
   {
      ProcessorTest.process(context -> assertEquals(context.getClassOrThrow("java.lang.Number"),
                                                      ((LM_Generic) context.getClassOrThrow("GenericsExample")
                                                                           .getGenericTypes()
                                                                           .get(0))
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
      ProcessorTest.process(context -> assertEquals(context.getClassOrThrow("GenericsExample"),
                                                      ((LM_Generic) context.getClassOrThrow("GenericsExample")
                                                                           .getGenericTypes()
                                                                           .get(0))
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
