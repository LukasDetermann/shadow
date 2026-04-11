package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.test.ProcessorTest.processorTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericTest
{
   @Test
   void getExtends()
   {
      processorTest().withCodeToCompile("GenericsExample.java",
                                        "public class GenericsExample<T extends Number>{}")
                     .process(context ->
                              {
                                 Ap.Class number = context.getClassOrThrow("java.lang.Number");
                                 Ap.Class genericsExample = context.getClassOrThrow("GenericsExample");
                                 Ap.Generic generic = (Ap.Generic) genericsExample.getGenericUsages().get(0);
                                 assertEquals(number, generic.getBound());
                              });
   }

   @Test
   void getEnclosing()
   {
      processorTest().withCodeToCompile("GenericsExample.java",
                                        "public class GenericsExample<T extends Number>{}")
                     .process(context ->
                              {
                                 Ap.Class genericsExample = context.getClassOrThrow("GenericsExample");
                                 Ap.Generic generic = (Ap.Generic) genericsExample.getGenericUsages().get(0);
                                 assertEquals(genericsExample, generic.getEnclosing());
                              });
   }
}
