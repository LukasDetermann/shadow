package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Record;
import org.determann.shadow.api.shadow.RecordComponent;
import org.junit.jupiter.api.Test;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecordComponentTest extends ShadowTest<RecordComponent>
{
   protected RecordComponentTest()
   {
      super(() -> SHADOW_API.getRecord("org.determann.shadow.example.processed.test.recordcomponent.RecordComponentExample")
                            .getRecordComponent("id"));
   }

   @Test
   void testIsSubtype()
   {
      assertTrue(getShadowSupplier().get().isSubtypeOf(SHADOW_API.getClass("java.lang.Number")));
   }

   @Test
   void testIsAssignableFrom()
   {
      assertTrue(getShadowSupplier().get().isAssignableFrom(SHADOW_API.getConstants().getPrimitiveLong()));
   }

   @Test
   void testGetRecord()
   {
      Record recordExample = SHADOW_API.getRecord("org.determann.shadow.example.processed.test.recordcomponent.RecordComponentExample");
      assertEquals(recordExample, recordExample.getRecordComponent("id").getRecord());
   }

   @Test
   void testGetType()
   {
      assertEquals(SHADOW_API.getClass("java.lang.Long"),
                   SHADOW_API.getRecord("org.determann.shadow.example.processed.test.recordcomponent.RecordComponentExample")
                             .getRecordComponent("id")
                             .getType());
   }

   @Test
   void testGetGetter()
   {
      assertEquals("id()",
                   SHADOW_API.getRecord("org.determann.shadow.example.processed.test.recordcomponent.RecordComponentExample")
                             .getRecordComponent("id")
                             .getGetter()
                             .toString());
   }

   @Test
   void testGetPackage()
   {
      assertEquals(SHADOW_API.getPackages("org.determann.shadow.example.processed.test.recordcomponent").get(0),
                   SHADOW_API.getRecord("org.determann.shadow.example.processed.test.recordcomponent.RecordComponentExample")
                             .getRecordComponent("id")
                             .getPackage());
   }
}
