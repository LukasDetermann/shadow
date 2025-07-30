package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.C;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.stream.Collectors;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ModuleTest
{
   @Test
   void getPackages()
   {
      test(implementation ->
           {
              C.Module module = requestOrThrow(implementation, GET_MODULE, "java.logging");
              Assertions.assertEquals(requestOrThrow(implementation, GET_PACKAGE, "java.util.logging"),
                                      requestOrThrow(module, MODULE_GET_PACKAGES));
           });
   }

   @Test
   void isOpen()
   {
      test(implementation ->
           {
              C.Module module = requestOrThrow(implementation, GET_MODULE, "java.logging");
              assertFalse(requestOrThrow(module, MODULE_IS_OPEN));
           });
   }

   @Test
   void isUnnamed()
   {
      test(implementation ->
           {
              C.Module module = requestOrThrow(implementation, GET_MODULE, "java.logging");
              assertFalse(requestOrThrow(module, MODULE_IS_UNNAMED));
           });
   }

   @Test
   void isAutomatic()
   {
      test(implementation ->
           {
              C.Module module = requestOrThrow(implementation, GET_MODULE, "java.logging");
              assertFalse(requestOrThrow(module, MODULE_IS_AUTOMATIC));
           });
   }

   @Test
   void getDirectives()
   {
      test(implementation ->
           {
              C.Module module = requestOrThrow(implementation, GET_MODULE, "java.logging");
              assertEquals("Requires {getDependency=java.base, isStatic=false, isTransitive=false}, " +
                           "Exports {getPackage=java.util.logging, getTargetModules=[]}, " +
                           "Provides {getService=Class {getQualifiedName=jdk.internal.logger.DefaultLoggerFinder, getModifiers=[PUBLIC]}, " +
                           "getImplementations=[Class {getQualifiedName=sun.util.logging.internal.LoggingProviderImpl, getModifiers=[PUBLIC, FINAL]}]}",
                    requestOrThrow(module, MODULE_GET_DIRECTIVES)
                          .stream()
                          .map(Objects::toString)
                          .collect(Collectors.joining(", ")));
           });
   }
}
