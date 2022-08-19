package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Primitive;
import org.junit.jupiter.api.Test;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrimitiveTest extends ShadowTest<Primitive>
{
   protected PrimitiveTest()
   {
      super(() -> SHADOW_API.getConstants().getPrimitiveInt());
   }

   @Test
   void testIsSubtypeOf()
   {
      assertTrue(SHADOW_API.getConstants().getPrimitiveInt().isAssignableFrom(SHADOW_API.getClass("java.lang.Number")));
      assertTrue(SHADOW_API.getConstants().getPrimitiveInt().isAssignableFrom(SHADOW_API.getConstants().getPrimitiveInt()));
      assertFalse(SHADOW_API.getConstants().getPrimitiveInt().isAssignableFrom(SHADOW_API.getClass("java.lang.String")));
   }

   @Test
   void testIsAssignableFrom()
   {
      assertTrue(SHADOW_API.getConstants().getPrimitiveInt().isAssignableFrom(SHADOW_API.getClass("java.lang.Number")));
      assertTrue(SHADOW_API.getConstants().getPrimitiveInt().isAssignableFrom(SHADOW_API.getConstants().getPrimitiveInt()));
      assertFalse(SHADOW_API.getConstants().getPrimitiveInt().isAssignableFrom(SHADOW_API.getClass("java.lang.String")));

      assertTrue(SHADOW_API.getConstants().getPrimitiveInt().isAssignableFrom(SHADOW_API.getConstants().getPrimitiveInt()));
   }
}
