package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.determann.shadow.api.annotation_processing.NestingKind.INNER;
import static io.determann.shadow.api.annotation_processing.NestingKind.OUTER;
import static io.determann.shadow.api.annotation_processing.test.ProcessorTest.processorTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeclaredTest
{
   @Test
   void getFormalGenerics()
   {
      processorTest().process(context ->
                              {
                                 Ap.Interface comparable = context.getInterfaceOrThrow("java.lang.Comparable");
                                 Ap.Generic generics = comparable.getGenericDeclarations().get(0);
                                 assertEquals("T", generics.getName());
                              });
   }

   @Test
   void getNesting()
   {
      processorTest().withCodeToCompile("NestingExample.java", "public class NestingExample {private class Inner{}}")
                     .process(context ->
                              {
                                 Ap.Class outer = context.getClassOrThrow("NestingExample");
                                 assertEquals(OUTER, outer.getNesting());

                                 Ap.Class inner = context.getClassOrThrow("NestingExample.Inner");
                                 assertEquals(INNER, inner.getNesting());
                              });
   }

   @Test
   void getFieldOrThrow()
   {
      processorTest().process(context ->
                              {
                                 Ap.Class math = context.getClassOrThrow("java.lang.Math");
                                 Ap.Field e = math.getFieldOrThrow("E");
                                 assertEquals(2.7182818284590452354D, e.getConstantValue());
                              });
   }

   @Test
   void getFields()
   {
      processorTest().withCodeToCompile("MyClass.java", "class MyClass{int a,b; private static final long C = 5;}")
                     .process(context ->
                              {
                                 Ap.Class myClass = context.getClassOrThrow("MyClass");
                                 List<String> fields = myClass.getFields()
                                                              .stream()
                                                              .map(Ap.Nameable::getName)
                                                              .toList();
                                 assertEquals(Arrays.asList("a", "b", "C"), fields);
                              });
   }

   @Test
   void getMethods()
   {
      processorTest().withCodeToCompile("MyClass.java",
                                        "class MyClass{int fooMethod(){return 0;}@Override public String toString() {return \"\";}}")
                     .process(context ->
                              {
                                 Ap.Class myClass = context.getClassOrThrow("MyClass");
                                 String methods = myClass.getMethods()
                                                         .stream()
                                                         .map(Ap.Nameable::getName)
                                                         .collect(Collectors.joining(", "));
                                 assertEquals("fooMethod, toString", methods);
                              });
   }

   @Test
   void getConstructors()
   {
      processorTest().withCodeToCompile("Test.java", "class Test{}")
                     .process(context ->
                              {
                                 Ap.Class cClass = context.getClassOrThrow("Test");
                                 assertEquals(1, cClass.getConstructors().size());
                              });
   }

   @Test
   void getPackage()
   {
      processorTest().process(context ->
                              {
                                 Ap.Class object = context.getClassOrThrow("java.lang.Object");
                                 Ap.Package cPackage = object.getPackage();
                                 assertEquals("java.lang", cPackage.getQualifiedName());
                              });
   }

   @Test
   void getBinaryName()
   {
      processorTest().process(context ->
                              {
                                 Ap.Class object = context.getClassOrThrow("java.lang.Object");
                                 assertEquals("java.lang.Object", object.getBinaryName());

                                 Ap.Class randomNumberGeneratorHolder = context.getClassOrThrow("java.lang.Math.RandomNumberGeneratorHolder");
                                 assertEquals("java.lang.Math$RandomNumberGeneratorHolder", randomNumberGeneratorHolder.getBinaryName());
                              });
   }
}
