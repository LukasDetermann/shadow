package io.determann.shadow.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.determann.shadow.api.annotation_processing.test.ProcessorTest.processorTest;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ModuleTest
{
   @Test
   void getPackages()
   {
      processorTest().process(context ->
                              {
                                 Ap.Module module = context.getModuleOrThrow("java.logging");
                                 Assertions.assertEquals(context.getPackage("java.util.logging"), module.getPackages());
                              });
   }

   @Test
   void isOpen()
   {
      processorTest().process(context ->
                              {
                                 Ap.Module module = context.getModuleOrThrow("java.logging");
                                 assertFalse(module.isOpen());
                              });
   }

   @Test
   void isUnnamed()
   {
      processorTest().process(context ->
                              {
                                 Ap.Module module = context.getModuleOrThrow("java.logging");
                                 assertFalse(module.isUnnamed());
                              });
   }

   @Test
   void isAutomatic()
   {
      processorTest().process(context ->
                              {
                                 Ap.Module module = context.getModuleOrThrow("java.logging");
                                 assertFalse(module.isAutomatic());
                              });
   }

   @Test
   void getDirectives()
   {
      processorTest().process(context ->
                              {
                                 Ap.Module module = context.getModuleOrThrow("java.logging");
                                 assertEquals("Requires{static=false, transitive=false, dependency=java.base}, " +
                                              "Exports{package=java.util.logging, targetModules=[]}, " +
                                              "Provides{service=Class{qualifiedName='jdk.internal.logger.DefaultLoggerFinder'}, " +
                                              "implementations=[Class{qualifiedName='sun.util.logging.internal.LoggingProviderImpl'}]}",
                                              module.getDirectives()
                                                    .stream()
                                                    .map(Objects::toString)
                                                    .collect(joining(", ")));
                              });
   }
}
