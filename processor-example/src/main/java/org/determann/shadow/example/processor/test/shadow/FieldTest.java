package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Declared;
import org.determann.shadow.api.shadow.Field;
import org.junit.jupiter.api.Test;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FieldTest extends VariableTest<Declared, Field>
{
   protected FieldTest()
   {
      super(() -> SHADOW_API.getClass("java.lang.String").getField("value"));
   }

   @Test
   void testGetSurrounding()
   {
      Class aClass = SHADOW_API.getClass("org.determann.shadow.example.processed.test.field.FieldExample");
      assertEquals(aClass, aClass.getField("ID").getSurrounding());
   }

   @Test
   void testIsConstant()
   {
      assertTrue(SHADOW_API.getClass("org.determann.shadow.example.processed.test.field.FieldExample")
                           .getField("ID")
                           .isConstant());
   }

   @Test
   void testGetConstantValue()
   {
      assertEquals(2, SHADOW_API.getClass("org.determann.shadow.example.processed.test.field.FieldExample")
                                .getField("ID")
                                .getConstantValue());
   }
}
