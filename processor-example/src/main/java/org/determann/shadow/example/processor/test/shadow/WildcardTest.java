package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.converter.ShadowConverter;
import org.determann.shadow.api.shadow.Wildcard;
import org.junit.jupiter.api.Test;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.*;

public class WildcardTest extends ShadowTest<Wildcard>
{
   protected WildcardTest()
   {
      super(() -> SHADOW_API.getConstants().getUnboundWildcard());
   }

   @Test
   void testGetExtends()
   {
      assertEquals(SHADOW_API.getClass("java.lang.Number"),
                   SHADOW_API.convert(SHADOW_API.getClass("org.determann.shadow.example.processed.test.wildcard.BoundsExample")
                                                .getMethods("extendsExample")
                                                .get(0)
                                                .getParameter("numbers")
                                                .getType())
                             .toInterface()
                             .map(anInterface -> anInterface.getGenerics().get(0))
                             .map(SHADOW_API::convert)
                             .flatMap(ShadowConverter::toWildcard)
                             .flatMap(Wildcard::getExtends)
                             .orElseThrow());
   }

   @Test
   void testGetSupper()
   {
      assertEquals(SHADOW_API.getClass("java.lang.Number"),
                   SHADOW_API.convert(SHADOW_API.getClass("org.determann.shadow.example.processed.test.wildcard.BoundsExample")
                                                .getMethods("superExample")
                                                .get(0)
                                                .getParameter("numbers")
                                                .getType())
                             .toInterface()
                             .map(anInterface -> anInterface.getGenerics().get(0))
                             .map(SHADOW_API::convert)
                             .flatMap(ShadowConverter::toWildcard)
                             .flatMap(Wildcard::getSuper)
                             .orElseThrow());
   }

   @Test
   void testContains()
   {
      assertTrue(SHADOW_API.convert(SHADOW_API.getClass("java.lang.Number"))
                           .asExtendsWildcard()
                           .contains(SHADOW_API.getClass("java.lang.Long")));

      assertFalse(SHADOW_API.convert(SHADOW_API.getClass("java.lang.Long"))
                            .asExtendsWildcard()
                            .contains(SHADOW_API.getClass("java.lang.Number")));

      assertTrue(SHADOW_API.convert(SHADOW_API.getClass("java.lang.Long"))
                           .asSuperWildcard()
                           .contains(SHADOW_API.getClass("java.lang.Number")));

      assertFalse(SHADOW_API.convert(SHADOW_API.getClass("java.lang.Number"))
                            .asSuperWildcard()
                            .contains(SHADOW_API.getClass("java.lang.Long")));
   }

   @Override
   void testRepresentsSameType()
   {
      assertFalse(getShadowSupplier().get().representsSameType(getShadowSupplier().get()));
      assertFalse(getShadowSupplier().get().representsSameType(SHADOW_API.getClass("java.util.jar.Attributes")));
   }
}
