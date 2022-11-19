package org.determann.shadow.api.shadow;

import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumConstantTest extends VariableTest<Enum, EnumConstant>
{
   EnumConstantTest()
   {
      super(shadowApi -> shadowApi.getEnumOrThrow("java.lang.annotation.RetentionPolicy").getEnumConstantOrThrow("SOURCE"));
   }

   @Test
   void testGetSurrounding()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 Enum anEnum = shadowApi.getEnumOrThrow("java.lang.annotation.RetentionPolicy");
                                 assertEquals(anEnum, anEnum.getEnumConstantOrThrow("SOURCE").getSurrounding());
                              })
                     .compile();
   }
}
