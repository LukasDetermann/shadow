package org.determann.shadow.api.shadow;

import org.determann.shadow.api.ElementBacked;
import org.determann.shadow.api.NestingKind;
import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class DeclaredTest<DECLARED extends Declared> extends ShadowTest<DECLARED>
{
   DeclaredTest(Function<ShadowApi, DECLARED> shadowSupplier)
   {
      super(shadowSupplier);
   }

   abstract void testisSubtypeOf();

   @Test
   void testGetFormalGenerics()
   {
      CompilationTest.process(shadowApi ->
                                    assertEquals("T",
                                                 shadowApi.getInterfaceOrThrow("java.lang.Comparable")
                                                          .getFormalGenerics()
                                                          .stream()
                                                          .map(Object::toString)
                                                          .collect(Collectors.joining())))
                     .compile();
   }

   @Test
   void testGetNesting()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(NestingKind.OUTER, shadowApi.getClassOrThrow("NestingExample").getNesting());
                                 assertEquals(NestingKind.INNER, shadowApi.getClassOrThrow("NestingExample.Inner").getNesting());
                              })
                     .withCodeToCompile("NestingExample.java",
                                        """
                                              public class NestingExample{
                                                 private class Inner{}
                                              }
                                              """)
                     .compile();
   }

   @Test
   void testgetFieldOrThrow()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(2.7182818284590452354D, shadowApi.getClassOrThrow("java.lang.Math").getFieldOrThrow("E").getConstantValue());
                                 assertThrows(NoSuchElementException.class, () -> shadowApi.getClassOrThrow("java.lang.Math").getFieldOrThrow("EEEE"));
                              })
                     .compile();
   }

   @Test
   void testGetFields()
   {
      CompilationTest.process(shadowApi ->
                                    assertEquals(Arrays.asList("a",
                                                               "b",
                                                               "C"),
                                                 shadowApi.getClassOrThrow("MyClass")
                                                          .getFields()
                                                          .stream()
                                                          .map(ElementBacked::getSimpleName)
                                                          .collect(Collectors.toList())))
                     .withCodeToCompile("MyClass.java", "class MyClass{int a,b; private static final long C = 5;}")
                     .compile();
   }

   @Test
   void testGetMethods()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(List.of("wait()",
                                                      "wait(long)",
                                                      "wait(long,int)"),
                                              shadowApi.getClassOrThrow("java.lang.Object").getMethods("wait")
                                                       .stream()
                                                       .map(Object::toString)
                                                       .sorted()
                                                       .toList());

                                 assertEquals(0, shadowApi.getClassOrThrow("java.lang.Object").getMethods("asdf").size());

                                 assertEquals(List.of("fooMethod()",
                                                      "toString()"),
                                              shadowApi.getClassOrThrow("MyClass")
                                                       .getMethods()
                                                       .stream()
                                                       .map(Object::toString)
                                                       .toList());
                              })
                     .withCodeToCompile("MyClass.java",
                                        "class MyClass{int fooMethod(){return 0;}@Override public String toString() {return \"\";}}")
                     .compile();
   }

   @Test
   void testGetConstructors()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(List.of("Object()"),
                                              shadowApi.getClassOrThrow("java.lang.Object").getConstructors().stream()
                                                       .map(Object::toString)
                                                       .toList());
                                 assertEquals(List.of("Math()"),
                                              shadowApi.getClassOrThrow("java.lang.Math").getConstructors().stream()
                                                       .map(Object::toString)
                                                       .toList());
                              })
                     .compile();
   }

   abstract void testGetDirectSuperTypes();

   abstract void testGetSuperTypes();

   @Test
   void testGetPackage()
   {
      CompilationTest.process(shadowApi ->
                                    assertEquals("java.lang",
                                                 shadowApi.getClassOrThrow("java.lang.Object").getPackage().toString()))
                     .compile();
   }

   @Test
   void testGetBinaryName()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 //Outer
                                 assertEquals("java.lang.Object", shadowApi.getClassOrThrow("java.lang.Object").getBinaryName());
                                 //Inner
                                 assertEquals("java.lang.Math$RandomNumberGeneratorHolder",
                                              shadowApi.getClassOrThrow("java.lang.Math.RandomNumberGeneratorHolder").getBinaryName());
                              })
                     .compile();
   }
}
