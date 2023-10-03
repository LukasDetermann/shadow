package io.determann.shadow.api.shadow;

import io.determann.shadow.api.test.ProcessorTest;
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
      ProcessorTest.process(shadowApi ->
                            {
                               Enum anEnum = shadowApi.getEnumOrThrow("java.lang.annotation.RetentionPolicy");
                               assertEquals(anEnum, anEnum.getEnumConstantOrThrow("SOURCE").getSurrounding());
                            })
                   .compile();
   }
}
