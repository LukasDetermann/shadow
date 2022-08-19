package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Record;
import org.determann.shadow.api.shadow.RecordComponent;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.*;

public class RecordTest extends DeclaredTest<Record>
{
   protected RecordTest()
   {
      super(() -> SHADOW_API.getRecord("org.determann.shadow.example.processed.test.record.RecordExample"));
   }

   @Test
   void testGetRecordComponent()
   {
      RecordComponent idComponent = getShadowSupplier().get().getRecordComponent("id");
      assertEquals("id", idComponent.getSimpleName());
      assertEquals(SHADOW_API.getClass("java.lang.Long"), idComponent.getType());

      assertThrows(NoSuchElementException.class, () -> getShadowSupplier().get().getRecordComponent("asdf"));
   }

   @Test
   void testGetDirectInterfaces()
   {
      assertEquals(List.of(SHADOW_API.getInterface("java.io.Serializable")), getShadowSupplier().get().getDirectInterfaces());
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      assertTrue(getShadowSupplier().get().isSubtypeOf(SHADOW_API.getClass("java.lang.Record")));
      assertTrue(getShadowSupplier().get().isSubtypeOf(getShadowSupplier().get()));
      assertFalse(getShadowSupplier().get().isSubtypeOf(SHADOW_API.getClass("java.lang.Number")));
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      assertEquals(List.of(SHADOW_API.getClass("java.lang.Record")),
                   SHADOW_API.getRecord("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.RecordNoParent")
                             .getDirectSuperTypes());

      assertEquals(List.of(SHADOW_API.getClass("java.lang.Record"),
                           SHADOW_API.getInterface("java.util.function.Consumer"),
                           SHADOW_API.getInterface("java.util.function.Supplier")),
                   SHADOW_API.getRecord("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.RecordMultiParent")
                             .getDirectSuperTypes());
   }

   @Test
   @Override
   void testGetSuperTypes()
   {
      assertEquals(Set.of(SHADOW_API.getClass("java.lang.Object"), SHADOW_API.getClass("java.lang.Record")),
                   SHADOW_API.getRecord("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.RecordNoParent")
                             .getSuperTypes());

      assertEquals(Set.of(SHADOW_API.getClass("java.lang.Object"),
                          SHADOW_API.getClass("java.lang.Record"),
                          SHADOW_API.getInterface("java.util.function.Consumer"),
                          SHADOW_API.getInterface("java.util.function.Supplier")),
                   SHADOW_API.getRecord("org.determann.shadow.example.processed.test.declared.DirektSuperTypeExample.RecordMultiParent")
                             .getSuperTypes());
   }
}
