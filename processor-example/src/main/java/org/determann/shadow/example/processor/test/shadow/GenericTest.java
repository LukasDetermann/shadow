package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Generic;
import org.junit.jupiter.api.Test;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenericTest extends ShadowTest<Generic>
{
   protected GenericTest()
   {
      super(() -> SHADOW_API.getInterface("java.lang.Comparable").getFormalGenerics().get(0));
   }

   @Test
   void testGetExtends()
   {
      assertEquals(SHADOW_API.getClass("java.lang.Number"),
                   SHADOW_API.convert(SHADOW_API.getClass("org.determann.shadow.example.processed.test.generics.GenericsExample")
                                                .getGenerics()
                                                .get(0))
                             .toGeneric()
                             .getExtends());
   }

   @Test
   void testGetEnclosing()
   {
      assertEquals(SHADOW_API.getClass("org.determann.shadow.example.processed.test.generics.GenericsExample"),
                   SHADOW_API.convert(SHADOW_API.getClass("org.determann.shadow.example.processed.test.generics.GenericsExample")
                                                .getGenerics()
                                                .get(0))
                             .toGeneric()
                             .getEnclosing());
   }

   @Test
   void testGetPackage()
   {
      assertEquals(SHADOW_API.getPackages("org.determann.shadow.example.processed.test.generics").get(0),
                   SHADOW_API.convert(SHADOW_API.getClass("org.determann.shadow.example.processed.test.generics.GenericsExample")
                                                .getGenerics()
                                                .get(0))
                             .toGeneric()
                             .getPackage());
   }
}
