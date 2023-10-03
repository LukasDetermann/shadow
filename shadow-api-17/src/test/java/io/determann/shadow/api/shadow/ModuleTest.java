package io.determann.shadow.api.shadow;

import io.determann.shadow.api.test.ProcessorTest;
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
      ProcessorTest.process(shadowApi -> assertEquals(shadowApi.getPackages("java.util.logging"),
                                                      shadowApi.getModuleOrThrow("java.logging").getPackages()))
                   .compile();
   }

   @Test
   void testIsOpen()
   {
      ProcessorTest.process(shadowApi -> assertTrue(shadowApi.getModules().stream().noneMatch(Module::isOpen)))
                   .compile();
   }

   @Test
   void testIsUnnamed()
   {
      ProcessorTest.process(shadowApi -> assertEquals(1, shadowApi.getModules().stream().filter(Module::isUnnamed).count()))
                   .compile();
   }

   @Test
   void testIsAutomatic()
   {
      ProcessorTest.process(shadowApi -> assertTrue(shadowApi.getModules().stream().noneMatch(Module::isAutomatic)))
                   .compile();
   }

   @Test
   void testGetDirectives()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals(
                                        "[Requires[[ACC_MANDATED (0x8000],java.base], Exports[java.util.logging], Provides[jdk.internal.logger.DefaultLoggerFinder,sun.util.logging.internal.LoggingProviderImpl]]",
                                        shadowApi.getModuleOrThrow("java.logging").getDirectives().toString()))
                   .compile();
   }
}
