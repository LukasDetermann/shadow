package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Enum;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.*;

public class EnumTest extends DeclaredTest<Enum>
{
   protected EnumTest()
   {
      super(() -> SHADOW_API.getEnum("java.lang.annotation.RetentionPolicy"));
   }
   @Test
   @Override
   void testisSubtypeOf()
   {
      assertTrue(getShadowSupplier().get().isSubtypeOf(SHADOW_API.getClass("java.lang.Object")));
      assertTrue(getShadowSupplier().get().isSubtypeOf(getShadowSupplier().get()));
      assertFalse(getShadowSupplier().get().isSubtypeOf(SHADOW_API.getClass("java.lang.Number")));
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      assertEquals(List.of(SHADOW_API.getClass("java.lang.Enum")),
                   SHADOW_API.getEnum("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.EnumNoParent")
                             .getDirectSuperTypes());

      assertEquals(List.of(SHADOW_API.getClass("java.lang.Enum"),
                           SHADOW_API.getInterface("java.util.function.Consumer"),
                           SHADOW_API.getInterface("java.util.function.Supplier")),
                   SHADOW_API.getEnum("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.EnumMultiParent")
                             .getDirectSuperTypes());
   }

   @Test
   @Override
   void testGetSuperTypes()
   {
      assertEquals(Set.of(SHADOW_API.getClass("java.lang.Object"),
                          SHADOW_API.getInterface("java.lang.constant.Constable"),
                          SHADOW_API.getInterface("java.lang.Comparable"),
                          SHADOW_API.getInterface("java.io.Serializable"),
                          SHADOW_API.getClass("java.lang.Enum")),
                   SHADOW_API.getEnum("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.EnumNoParent")
                             .getSuperTypes());

      assertEquals(Set.of(SHADOW_API.getClass("java.lang.Object"),
                          SHADOW_API.getInterface("java.lang.constant.Constable"),
                          SHADOW_API.getInterface("java.lang.Comparable"),
                          SHADOW_API.getInterface("java.io.Serializable"),
                          SHADOW_API.getClass("java.lang.Enum"),
                          SHADOW_API.getInterface("java.util.function.Consumer"),
                          SHADOW_API.getInterface("java.util.function.Supplier")),
                   SHADOW_API.getEnum("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.EnumMultiParent")
                             .getSuperTypes());
   }
}
