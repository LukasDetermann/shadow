package org.determann.shadow.api.shadow;

import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModuleTest extends ShadowTest<Module>
{
   ModuleTest()
   {
      super(shadowApi -> shadowApi.getModuleOrThrow("java.base"));
   }

   @Test
   void testGetPackages()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getPackagesOrThrow("java.util.logging"),
                                                        shadowApi.getModuleOrThrow("java.logging").getPackages()))
                     .compile();
   }

   @Test
   void testIsOpen()
   {
      CompilationTest.process(shadowApi -> assertTrue(shadowApi.getModules().stream().noneMatch(Module::isOpen)))
                     .compile();
   }

   @Test
   void testIsUnnamed()
   {
      CompilationTest.process(shadowApi -> assertEquals(1, shadowApi.getModules().stream().filter(Module::isUnnamed).count()))
                     .compile();
   }

   @Test
   void testGetDirectives()
   {
      CompilationTest.process(shadowApi ->
                                    assertEquals(
                                          "[Requires[[ACC_MANDATED (0x8000],java.base], Exports[java.util.logging], Provides[jdk.internal.logger.DefaultLoggerFinder,sun.util.logging.internal.LoggingProviderImpl]]",
                                          shadowApi.getModuleOrThrow("java.logging").getDirectives().toString()))
                     .compile();
   }
}
