package io.determann.shadow.tck.internal.shadow.type;

import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.structure.C_Package;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Interface;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.shadow.C_NestingKind.INNER;
import static io.determann.shadow.api.shadow.C_NestingKind.OUTER;
import static io.determann.shadow.tck.internal.TckTest.test;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeclaredTest
{
   @Test
   void getFormalGenerics()
   {
      test(implementation ->
           {
              C_Interface comparable = requestOrThrow(implementation, GET_INTERFACE, "java.lang.Comparable");
              C_Generic generics = requestOrThrow(comparable, INTERFACE_GET_GENERICS).get(0);
              assertEquals("T", requestOrThrow(generics, NAMEABLE_GET_NAME));
           });
   }

   @Test
   void getNesting()
   {
      withSource("NestingExample.java", "public class NestingExample {private class Inner{}}")
            .test(implementation ->
                  {
                     C_Class outer = requestOrThrow(implementation, GET_CLASS, "NestingExample");
                     assertEquals(OUTER, requestOrThrow(outer, DECLARED_GET_NESTING));

                     C_Class inner = requestOrThrow(implementation, GET_CLASS, "NestingExample.Inner");
                     assertEquals(INNER, requestOrThrow(inner, DECLARED_GET_NESTING));
                  });
   }

   @Test
   void getFieldOrThrow()
   {
      test(implementation ->
           {
              C_Class math = requestOrThrow(implementation, GET_CLASS, "java.lang.Math");
              C_Field e = requestOrThrow(math, DECLARED_GET_FIELD, "E");
              assertEquals(2.7182818284590452354D, requestOrThrow(e, FIELD_GET_CONSTANT_VALUE));
           });
   }

   @Test
   void getFields()
   {
      withSource("MyClass.java", "class MyClass{int a,b; private static final long C = 5;}")
            .test(implementation ->
                  {
                     C_Class myClass = requestOrThrow(implementation, GET_CLASS, "MyClass");
                     List<String> fields = requestOrThrow(myClass, DECLARED_GET_FIELDS)
                           .stream()
                           .map(field -> requestOrThrow(field, NAMEABLE_GET_NAME))
                           .toList();
                     assertEquals(Arrays.asList("a", "b", "C"), fields);
                  });
   }

   @Test
   void getMethods()
   {
      withSource("MyClass.java", "class MyClass{int fooMethod(){return 0;}@Override public String toString() {return \"\";}}")
            .test(implementation ->
                  {
                     C_Class myClass = requestOrThrow(implementation, GET_CLASS, "MyClass");
                     String methods = requestOrThrow(myClass, DECLARED_GET_METHODS)
                           .stream()
                           .map(method -> requestOrThrow(method, NAMEABLE_GET_NAME))
                           .collect(Collectors.joining(", "));
                     assertEquals("fooMethod, toString", methods);
                  });
   }

   @Test
   void getConstructors()
   {
      withSource("Test.java", "class Test{}")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Test");
                     assertEquals(1, requestOrThrow(cClass, CLASS_GET_CONSTRUCTORS).size());
                  });
   }

   @Test
   void getPackage()
   {
      test(implementation ->
           {
              C_Class object = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");
              C_Package cPackage = requestOrThrow(object, DECLARED_GET_PACKAGE);
              assertEquals("java.lang", requestOrThrow(cPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));
           });
   }

   @Test
   void getBinaryName()
   {
      test(implementation ->
           {
              C_Class object = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");
              assertEquals("java.lang.Object", requestOrThrow(object, DECLARED_GET_BINARY_NAME));

              C_Class randomNumberGeneratorHolder = requestOrThrow(implementation, GET_CLASS, "java.lang.Math.RandomNumberGeneratorHolder");
              assertEquals("java.lang.Math$RandomNumberGeneratorHolder", requestOrThrow(randomNumberGeneratorHolder, DECLARED_GET_BINARY_NAME));
           });
   }
}
