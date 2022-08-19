package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Module;
import org.junit.jupiter.api.Test;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModuleTest extends ShadowTest<Module>
{
   protected ModuleTest()
   {
      super(() -> SHADOW_API.getModule("java.base"));
   }

   @Test
   void testGetPackages()
   {
      assertEquals(SHADOW_API.getPackages("java.util.logging"), SHADOW_API.getModule("java.logging").getPackages());
   }

   @Test
   void testIsOpen()
   {
      assertTrue(SHADOW_API.getModules().stream().noneMatch(Module::isOpen));
   }

   @Test
   void testIsUnnamed()
   {
      assertEquals(1, SHADOW_API.getModules().stream().filter(Module::isUnnamed).count());
   }

   @Test
   void testIsAutomatic()
   {
      assertTrue(SHADOW_API.getModules().stream().noneMatch(Module::isAutomatic));
   }

   @Test
   void testGetDirectives()
   {
      assertEquals(
            "[Requires[[ACC_MANDATED (0x8000],java.base], Exports[java.util.logging], Provides[jdk.internal.logger.DefaultLoggerFinder,sun.util.logging.internal.LoggingProviderImpl]]",
            SHADOW_API.getModule("java.logging").getDirectives().toString());
   }
}
