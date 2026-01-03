package io.determann.shadow.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ModuleTest
{
   @Test
   void getPackages()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Module module = context.getModuleOrThrow("java.logging");
                               Assertions.assertEquals(context.getPackage("java.util.logging"), module.getPackages());
                            })
                   .compile();
   }

   @Test
   void isOpen()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Module module = context.getModuleOrThrow("java.logging");
                               assertFalse(module.isOpen());
                            })
                   .compile();
   }

   @Test
   void isUnnamed()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Module module = context.getModuleOrThrow("java.logging");
                               assertFalse(module.isUnnamed());
                            })
                   .compile();
   }

   @Test
   void isAutomatic()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Module module = context.getModuleOrThrow("java.logging");
                               assertFalse(module.isAutomatic());
                            })
                   .compile();
   }

   @Test
   void getDirectives()
   {
      ProcessorTest.process(context ->
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
                            })
                   .compile();
   }
}
