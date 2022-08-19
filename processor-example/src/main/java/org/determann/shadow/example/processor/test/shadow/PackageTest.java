package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Package;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PackageTest extends ShadowTest<Package>
{
   protected PackageTest()
   {
      super(() -> SHADOW_API.getPackage("java.base", "java.lang"));
   }

   @Test
   void testGetContent()
   {
      assertTrue(SHADOW_API.getPackages("asdkfh").isEmpty());

      assertEquals(List.of(SHADOW_API.getClass("org.determann.shadow.example.processed.test.packagee.not_empty.AnyClass")),
                   SHADOW_API.getPackages("org.determann.shadow.example.processed.test.packagee.not_empty").get(0).getContent());
   }
}
