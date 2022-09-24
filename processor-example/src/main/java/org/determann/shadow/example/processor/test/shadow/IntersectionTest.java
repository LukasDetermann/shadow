package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Intersection;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntersectionTest extends ShadowTest<Intersection>
{
   protected IntersectionTest()
   {
      super(() -> SHADOW_API.convert(SHADOW_API.getClass("org.determann.shadow.example.processed.test.intersection.IntersectionExample")
                                               .getFormalGenerics()
                                               .get(0)
                                               .getExtends())
                            .toIntersection());
   }

   @Test
   void testGetBounds()
   {
      assertEquals(List.of(SHADOW_API.getInterface("java.util.Collection"), SHADOW_API.getInterface("java.io.Serializable")),
                   getShadowSupplier().get().getBounds());
   }
}
