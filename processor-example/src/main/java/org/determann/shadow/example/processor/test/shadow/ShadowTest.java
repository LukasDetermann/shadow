package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Shadow;
import org.junit.jupiter.api.Test;

import javax.lang.model.type.TypeMirror;
import java.util.function.Supplier;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.*;

abstract class ShadowTest<SHADOW extends Shadow<? extends TypeMirror>>
{
   private final Supplier<SHADOW> shadowSupplier;

   protected ShadowTest(Supplier<SHADOW> shadowSupplier) {this.shadowSupplier = shadowSupplier;}

   @Test
   void testRepresentsSameType()
   {
      assertTrue(getShadowSupplier().get().representsSameType(getShadowSupplier().get()));
      assertFalse(getShadowSupplier().get().representsSameType(SHADOW_API.getClass("java.util.jar.Attributes")));
      assertFalse(getShadowSupplier().get().representsSameType(SHADOW_API.getConstants().getUnboundWildcard()));
   }

   @Test
   void testEquals()
   {
      assertTrue(getShadowSupplier().get().equals(getShadowSupplier().get()));
      assertFalse(getShadowSupplier().get().equals(SHADOW_API.getClass("java.util.jar.Attributes")));
   }

   @Test
   void testErasure()
   {
      assertEquals(SHADOW_API.getInterface("java.util.Collection"),
                   SHADOW_API.getInterface("java.util.Collection").withGenerics("java.lang.Object").erasure());

      assertEquals(SHADOW_API.getInterface("java.util.Collection"),
                   SHADOW_API.getInterface("java.util.Collection").withGenerics("java.lang.Object"));

   }

   protected Supplier<SHADOW> getShadowSupplier()
   {
      return shadowSupplier;
   }
}
