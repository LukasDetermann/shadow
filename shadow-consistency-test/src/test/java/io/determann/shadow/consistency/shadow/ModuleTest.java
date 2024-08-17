package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.structure.ModuleLangModel;
import io.determann.shadow.api.shadow.structure.Module;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModuleTest extends ShadowTest<Module>
{
   ModuleTest()
   {
      super(context -> context.getModuleOrThrow("java.base"));
   }

   @Test
   void testGetPackages()
   {
      ProcessorTest.process(context -> assertEquals(context.getPackages("java.util.logging"),
                                                      context.getModuleOrThrow("java.logging").getPackages()))
                   .compile();
   }

   @Test
   void testIsOpen()
   {
      ProcessorTest.process(context -> assertTrue(context.getModules().stream().noneMatch(ModuleLangModel::isOpen))).compile();
   }

   @Test
   void testIsUnnamed()
   {
      ProcessorTest.process(context -> assertEquals(1, context.getModules().stream().filter(ModuleLangModel::isUnnamed).count())).compile();
   }

   @Test
   void testIsAutomatic()
   {
      ProcessorTest.process(context -> assertTrue(context.getModules().stream().noneMatch(ModuleLangModel::isAutomatic))).compile();
   }

   @Test
   void testGetDirectives()
   {
      ProcessorTest.process(context ->
                                  assertEquals(
                                        "[Requires {getDependency=java.base, isStatic=false, isTransitive=false}, " +
                                        "Exports {getPackage=java.util.logging, getTargetModules=[]}, " +
                                        "Provides {getService=Class {getQualifiedName=jdk.internal.logger.DefaultLoggerFinder, getKind=CLASS, " +
                                        "getModifiers=[PUBLIC]}, getImplementations=[" +
                                        "Class {getQualifiedName=sun.util.logging.internal.LoggingProviderImpl, getKind=CLASS, getModifiers=[PUBLIC, FINAL]}]}]",
                                        context.getModuleOrThrow("java.logging").getDirectives().toString()))
                   .compile();
   }
}
