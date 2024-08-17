package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.NameableLangModel;
import io.determann.shadow.api.shadow.type.Declared;
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
   DeclaredTest(Function<AnnotationProcessingContext, DECLARED> shadowSupplier)
   {
      super(shadowSupplier);
   }

   abstract void testisSubtypeOf();

   @Test
   void testGetFormalGenerics()
   {
      ProcessorTest.process(context ->
                                  assertEquals("T",
                                               context.getInterfaceOrThrow("java.lang.Comparable")
                                                        .getGenerics()
                                                        .stream()
                                                        .map(NameableLangModel::getName)
                                                        .collect(Collectors.joining())))
                   .compile();
   }

   @Test
   void testGetNesting()
   {
      ProcessorTest.process(context ->
                            {
                               assertEquals(io.determann.shadow.api.shadow.NestingKind.OUTER, context.getClassOrThrow("NestingExample").getNesting());
                               assertEquals(io.determann.shadow.api.shadow.NestingKind.INNER, context.getClassOrThrow("NestingExample.Inner").getNesting());
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
      ProcessorTest.process(context ->
                            {
                               assertEquals(2.7182818284590452354D,
                                            context.getClassOrThrow("java.lang.Math").getFieldOrThrow("E").getConstantValue());
                               assertThrows(NoSuchElementException.class,
                                            () -> context.getClassOrThrow("java.lang.Math").getFieldOrThrow("EEEE"));
                            })
                   .compile();
   }

   @Test
   void testGetFields()
   {
      ProcessorTest.process(context ->
                                  assertEquals(Arrays.asList("a",
                                                             "b",
                                                             "C"),
                                               context.getClassOrThrow("MyClass")
                                                        .getFields()
                                                        .stream()
                                                        .map(NameableLangModel::getName)
                                                        .collect(Collectors.toList())))
                   .withCodeToCompile("MyClass.java", "class MyClass{int a,b; private static final long C = 5;}")
                   .compile();
   }

   @Test
   void testGetMethods()
   {
      ProcessorTest.process(context ->
                            {
                               assertEquals(List.of("wait()",
                                                    "wait(long)",
                                                    "wait(long,int)"),
                                            context.getClassOrThrow("java.lang.Object").getMethods("wait")
                                                     .stream()
                                                     .map(Object::toString)
                                                     .sorted()
                                                     .toList());

                               assertEquals(0, context.getClassOrThrow("java.lang.Object").getMethods("asdf").size());

                               assertEquals(List.of("fooMethod()",
                                                    "toString()"),
                                            context.getClassOrThrow("MyClass")
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
      ProcessorTest.process(context ->
                            {
                               assertEquals(List.of("Object()"),
                                            context.getClassOrThrow("java.lang.Object").getConstructors().stream()
                                                     .map(Object::toString)
                                                     .toList());
                               assertEquals(List.of("Math()"),
                                            context.getClassOrThrow("java.lang.Math").getConstructors().stream()
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
      ProcessorTest.process(context ->
                                  assertEquals("java.lang",
                                               context.getClassOrThrow("java.lang.Object").getPackage().toString()))
                   .compile();
   }

   @Test
   void testGetBinaryName()
   {
      ProcessorTest.process(context ->
                            {
                               //Outer
                               assertEquals("java.lang.Object", context.getClassOrThrow("java.lang.Object").getBinaryName());
                               //Inner
                               assertEquals("java.lang.Math$RandomNumberGeneratorHolder",
                                            context.getClassOrThrow("java.lang.Math.RandomNumberGeneratorHolder").getBinaryName());
                            })
                   .compile();
   }
}
