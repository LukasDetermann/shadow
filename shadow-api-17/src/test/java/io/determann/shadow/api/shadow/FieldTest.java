package io.determann.shadow.api.shadow;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldTest extends VariableTest<Declared, Field>
{
   FieldTest()
   {
      super(shadowApi -> shadowApi.getClassOrThrow("java.lang.String").getFieldOrThrow("value"));
   }

   @Test
   void testGetSurrounding()
   {
      ProcessorTest.process(shadowApi ->
                              {
                                 Class aClass = shadowApi.getClassOrThrow("FieldExample");
                                 assertEquals(aClass, aClass.getFieldOrThrow("ID").getSurrounding());
                              })
                   .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                   .compile();
   }

   @Test
   void testIsConstant()
   {
      ProcessorTest.process(shadowApi -> assertTrue(shadowApi.getClassOrThrow("FieldExample")
                                                             .getFieldOrThrow("ID")
                                                             .isConstant()))
                   .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                   .compile();
   }

   @Test
   void testGetConstantValue()
   {
      ProcessorTest.process(shadowApi -> assertEquals(2, shadowApi.getClassOrThrow("FieldExample")
                                                                  .getFieldOrThrow("ID")
                                                                  .getConstantValue()))
                   .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                   .compile();
   }
}
