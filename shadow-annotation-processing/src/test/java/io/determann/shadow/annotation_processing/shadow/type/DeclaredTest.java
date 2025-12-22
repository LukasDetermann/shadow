package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.determann.shadow.api.NestingKind.INNER;
import static io.determann.shadow.api.NestingKind.OUTER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeclaredTest
{
   @Test
   void getFormalGenerics()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Interface comparable = context.getInterfaceOrThrow("java.lang.Comparable");
                               Ap.Generic generics = comparable.getGenericDeclarations().get(0);
                               assertEquals("T", generics.getName());
                            })
                   .compile();
   }

   @Test
   void getNesting()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class outer = context.getClassOrThrow("NestingExample");
                               assertEquals(OUTER, outer.getNesting());

                               Ap.Class inner = context.getClassOrThrow("NestingExample.Inner");
                               assertEquals(INNER, inner.getNesting());
                            })
                   .withCodeToCompile("NestingExample.java", "public class NestingExample {private class Inner{}}")
                   .compile();
   }

   @Test
   void getFieldOrThrow()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class math = context.getClassOrThrow("java.lang.Math");
                               Ap.Field e = math.getFieldOrThrow("E");
                               assertEquals(2.7182818284590452354D, e.getConstantValue());
                            })
                   .compile();
   }

   @Test
   void getFields()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class myClass = context.getClassOrThrow("MyClass");
                               List<String> fields = myClass.getFields()
                                                            .stream()
                                                            .map(Ap.Nameable::getName)
                                                            .toList();
                               assertEquals(Arrays.asList("a", "b", "C"), fields);
                            })
                   .withCodeToCompile("MyClass.java", "class MyClass{int a,b; private static final long C = 5;}")
                   .compile();
   }

   @Test
   void getMethods()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class myClass = context.getClassOrThrow("MyClass");
                               String methods = myClass.getMethods()
                                                       .stream()
                                                       .map(Ap.Nameable::getName)
                                                       .collect(Collectors.joining(", "));
                               assertEquals("fooMethod, toString", methods);
                            })
                   .withCodeToCompile("MyClass.java",
                                      "class MyClass{int fooMethod(){return 0;}@Override public String toString() {return \"\";}}")
                   .compile();
   }

   @Test
   void getConstructors()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("Test");
                               assertEquals(1, cClass.getConstructors().size());
                            })
                   .withCodeToCompile("Test.java", "class Test{}")
                   .compile();
   }

   @Test
   void getPackage()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class object = context.getClassOrThrow("java.lang.Object");
                               Ap.Package cPackage = object.getPackage();
                               assertEquals("java.lang", cPackage.getQualifiedName());
                            })
                   .compile();
   }

   @Test
   void getBinaryName()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class object = context.getClassOrThrow("java.lang.Object");
                               assertEquals("java.lang.Object", object.getBinaryName());

                               Ap.Class randomNumberGeneratorHolder = context.getClassOrThrow("java.lang.Math.RandomNumberGeneratorHolder");
                               assertEquals("java.lang.Math$RandomNumberGeneratorHolder", randomNumberGeneratorHolder.getBinaryName());
                            })
                   .compile();
   }
}
