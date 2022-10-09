package org.determann.shadow.api.shadow;

import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldTest extends VariableTest<Declared, Field>
{
   FieldTest()
   {
      super(shadowApi -> shadowApi.getClass("java.lang.String").getField("value"));
   }

   @Test
   void testGetSurrounding()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 Class aClass = shadowApi.getClass("FieldExample");
                                 assertEquals(aClass, aClass.getField("ID").getSurrounding());
                              })
                     .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                     .compile();
   }

   @Test
   void testIsConstant()
   {
      CompilationTest.process(shadowApi -> assertTrue(shadowApi.getClass("FieldExample")
                                                               .getField("ID")
                                                               .isConstant()))
                     .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                     .compile();
   }

   @Test
   void testGetConstantValue()
   {
      CompilationTest.process(shadowApi -> assertEquals(2, shadowApi.getClass("FieldExample")
                                                                    .getField("ID")
                                                                    .getConstantValue()))
                     .withCodeToCompile("FieldExample.java", "public class FieldExample{public static final int ID = 2;}")
                     .compile();
   }
}
