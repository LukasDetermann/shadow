package io.determann.shadow.tck.internal.shadow.type;

import io.determann.shadow.api.C;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.determann.shadow.api.NestingKind.INNER;
import static io.determann.shadow.api.NestingKind.OUTER;
import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
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
              C.Interface comparable = requestOrThrow(implementation, GET_INTERFACE, "java.lang.Comparable");
              C.Generic generics = requestOrThrow(comparable, INTERFACE_GET_GENERIC_DECLARATIONS).get(0);
              assertEquals("T", requestOrThrow(generics, NAMEABLE_GET_NAME));
           });
   }

   @Test
   void getNesting()
   {
      withSource("NestingExample.java", "public class NestingExample {private class Inner{}}")
            .test(implementation ->
                  {
                     C.Class outer = requestOrThrow(implementation, GET_CLASS, "NestingExample");
                     assertEquals(OUTER, requestOrThrow(outer, DECLARED_GET_NESTING));

                     C.Class inner = requestOrThrow(implementation, GET_CLASS, "NestingExample.Inner");
                     assertEquals(INNER, requestOrThrow(inner, DECLARED_GET_NESTING));
                  });
   }

   @Test
   void getFieldOrThrow()
   {
      test(implementation ->
           {
              C.Class math = requestOrThrow(implementation, GET_CLASS, "java.lang.Math");
              C.Field e = requestOrThrow(math, DECLARED_GET_FIELD, "E");
              assertEquals(2.7182818284590452354D, requestOrThrow(e, FIELD_GET_CONSTANT_VALUE));
           });
   }

   @Test
   void getFields()
   {
      withSource("MyClass.java", "class MyClass{int a,b; private static final long C = 5;}")
            .test(implementation ->
                  {
                     C.Class myClass = requestOrThrow(implementation, GET_CLASS, "MyClass");
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
                     C.Class myClass = requestOrThrow(implementation, GET_CLASS, "MyClass");
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
                     C.Class cClass = requestOrThrow(implementation, GET_CLASS, "Test");
                     assertEquals(1, requestOrThrow(cClass, CLASS_GET_CONSTRUCTORS).size());
                  });
   }

   @Test
   void getPackage()
   {
      test(implementation ->
           {
              C.Class object = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");
              C.Package cPackage = requestOrThrow(object, DECLARED_GET_PACKAGE);
              assertEquals("java.lang", requestOrThrow(cPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));
           });
   }

   @Test
   void getBinaryName()
   {
      test(implementation ->
           {
              C.Class object = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");
              assertEquals("java.lang.Object", requestOrThrow(object, DECLARED_GET_BINARY_NAME));

              C.Class randomNumberGeneratorHolder = requestOrThrow(implementation, GET_CLASS, "java.lang.Math.RandomNumberGeneratorHolder");
              assertEquals("java.lang.Math$RandomNumberGeneratorHolder", requestOrThrow(randomNumberGeneratorHolder, DECLARED_GET_BINARY_NAME));
           });
   }
}
