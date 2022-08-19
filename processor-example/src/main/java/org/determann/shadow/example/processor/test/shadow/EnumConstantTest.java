package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.EnumConstant;
import org.junit.jupiter.api.Test;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnumConstantTest extends VariableTest<Enum, EnumConstant>
{
   protected EnumConstantTest()
   {
      super(() -> SHADOW_API.getEnum("java.lang.annotation.RetentionPolicy").getEnumConstant("SOURCE"));
   }

   @Test
   void testGetSurrounding()
   {
      Enum anEnum = SHADOW_API.getEnum("java.lang.annotation.RetentionPolicy");
      assertEquals(anEnum, anEnum.getEnumConstant("SOURCE").getSurrounding());
   }
}
