package io.determann.shadow.consistency.shadow.type;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.type.LM_Intersection;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntersectionTest extends TypeTest<LM_Intersection>
{
   IntersectionTest()
   {
      super(context -> ((LM_Intersection) context.getClassOrThrow("IntersectionExample")
                                                 .getGenerics()
                                                 .get(0).getExtends()));
   }

   @Test
   void testGetBounds()
   {
      ProcessorTest.process(context -> assertEquals(List.of(context.getInterfaceOrThrow("java.util.Collection"),
                                                              context.getInterfaceOrThrow("java.io.Serializable")),
                                                    getTypeSupplier().apply(context).getBounds()))
                   .withCodeToCompile("IntersectionExample.java",
                                      "public class IntersectionExample<T extends java.util.Collection & java.io.Serializable>{\n}")
                   .compile();
   }
}
