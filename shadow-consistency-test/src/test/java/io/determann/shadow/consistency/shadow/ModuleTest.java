package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.LangModelQueries;
import io.determann.shadow.api.lang_model.shadow.structure.ModuleLangModel;
import io.determann.shadow.api.shadow.structure.Module;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
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
                                                      query(shadowApi.getModuleOrThrow("java.logging")).getPackages()))
                   .compile();
   }

   @Test
   void testIsOpen()
   {
      ProcessorTest.process(context -> assertTrue(context.getModules().stream().map(LangModelQueries::query).noneMatch(ModuleLangModel::isOpen)))
                   .compile();
   }

   @Test
   void testIsUnnamed()
   {
      ProcessorTest.process(shadowApi -> assertEquals(1, shadowApi.getModules().stream().map(LangModelQueries::query).filter(ModuleLangModel::isUnnamed).count()))
                   .compile();
   }

   @Test
   void testIsAutomatic()
   {
      ProcessorTest.process(shadowApi -> assertTrue(shadowApi.getModules().stream().map(LangModelQueries::query).noneMatch(ModuleLangModel::isAutomatic)))
                   .compile();
   }

   @Test
   void testGetDirectives()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals(
                                        "[Requires[[ACC_MANDATED (0x8000],java.base], Exports[java.util.logging], Provides[jdk.internal.logger.DefaultLoggerFinder,sun.util.logging.internal.LoggingProviderImpl]]",
                                        query(shadowApi.getModuleOrThrow("java.logging")).getDirectives().toString()))
                   .compile();
   }
}
