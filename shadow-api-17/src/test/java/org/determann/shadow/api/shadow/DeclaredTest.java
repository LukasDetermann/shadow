package org.determann.shadow.api.shadow;

import org.determann.shadow.api.ElementBacked;
import org.determann.shadow.api.NestingKind;
import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

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
                                                 shadowApi.getInterface("java.lang.Comparable")
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
                                 assertEquals(NestingKind.OUTER, shadowApi.getClass("NestingExample").getNesting());
                                 assertEquals(NestingKind.INNER, shadowApi.getClass("NestingExample.Inner").getNesting());
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
   void testGetField()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(2.7182818284590452354D, shadowApi.getClass("java.lang.Math").getField("E").getConstantValue());
                                 assertThrows(NoSuchElementException.class, () -> shadowApi.getClass("java.lang.Math").getField("EEEE"));
                              })
                     .compile();
   }

   @Test
   void testGetFields()
   {
      CompilationTest.process(shadowApi ->
                                    assertEquals(List.of("E",
                                                         "PI",
                                                         "DEGREES_TO_RADIANS",
                                                         "RADIANS_TO_DEGREES",
                                                         "negativeZeroFloatBits",
                                                         "negativeZeroDoubleBits",
                                                         "twoToTheDoubleScaleUp",
                                                         "twoToTheDoubleScaleDown"),
                                                 shadowApi.getClass("java.lang.Math")
                                                          .getFields()
                                                          .stream()
                                                          .map(ElementBacked::getSimpleName)
                                                          .toList()))
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
                                              shadowApi.getClass("java.lang.Object").getMethods("wait")
                                                       .stream()
                                                       .map(Object::toString)
                                                       .toList());

                                 assertEquals(0, shadowApi.getClass("java.lang.Object").getMethods("asdf").size());

                                 assertEquals(
                                       List.of("getClass()",
                                               "hashCode()",
                                               "equals(java.lang.Object)",
                                               "clone()",
                                               "toString()",
                                               "notify()",
                                               "notifyAll()",
                                               "wait()",
                                               "wait(long)",
                                               "wait(long,int)",
                                               "finalize()"),
                                       shadowApi.getClass("java.lang.Object").getMethods().stream().map(Object::toString).toList());
                              })
                     .compile();
   }

   @Test
   void testGetConstructors()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(List.of("Object()"),
                                              shadowApi.getClass("java.lang.Object").getConstructors().stream()
                                                       .map(Object::toString)
                                                       .toList());
                                 assertEquals(List.of("Math()"),
                                              shadowApi.getClass("java.lang.Math").getConstructors().stream()
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
                                                 shadowApi.getClass("java.lang.Object").getPackage().toString()))
                     .compile();
   }

   @Test
   void testGetBinaryName()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 //Outer
                                 assertEquals("java.lang.Object", shadowApi.getClass("java.lang.Object").getBinaryName());
                                 //Inner
                                 assertEquals("java.lang.Math$RandomNumberGeneratorHolder",
                                              shadowApi.getClass("java.lang.Math.RandomNumberGeneratorHolder").getBinaryName());
                              })
                     .compile();
   }
}
