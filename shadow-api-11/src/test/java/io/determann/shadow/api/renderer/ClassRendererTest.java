package io.determann.shadow.api.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.consistency.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.compileTime(context -> context.getClassOrThrow("java.lang.Object"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("java.lang.Object")))
                     .test(aClass ->
                           {
                              assertEquals("public class Object {}\n", render(DEFAULT, aClass).declaration());
                              assertEquals("public class Object {\ntest\n}\n", render(DEFAULT, aClass).declaration("test"));
                           });

      ConsistencyTest.compileTime(context -> context.getClassOrThrow("InterpolateGenericsExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("InterpolateGenericsExample")))
                     .withCode("InterpolateGenericsExample.java",
                               "public class InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {\n" +
                               "     static class IndependentGeneric<C> {}\n" +
                               "     static class DependentGeneric<D extends E, E> {}\n" +
                               "  }\n")
                     .test(aClass -> assertEquals(
                           "public class InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {}\n",
                           render(DEFAULT, aClass).declaration()));

      ConsistencyTest.compileTime(context -> context.getClassOrThrow("ClassParent"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("ClassParent")))
                     .withCode("ClassParent.java", "@TestAnnotation\nabstract class ClassParent extends Number {}")
                     .withCode("TestAnnotation.java", "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface TestAnnotation{}")
                     .test(aClass -> assertEquals("@TestAnnotation\nabstract class ClassParent extends Number {}\n",
                                                  render(DEFAULT, aClass).declaration()));

      ConsistencyTest.compileTime(context -> context.getClassOrThrow("ClassMixedParent"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("ClassMixedParent")))
                     .withCode("ClassMixedParent.java",
                               "abstract class ClassMixedParent extends Number implements java.lang.Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}")
                     .withCode("TestAnnotation.java", "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface TestAnnotation{}")
                     .test(aClass -> assertEquals(
                           "abstract class ClassMixedParent extends Number implements Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}\n",
                           render(DEFAULT, aClass).declaration()),
                           aClass -> assertEquals(
                                 "abstract class ClassMixedParent extends Number implements Comparable, java.util.function.Consumer {}\n",
                                 render(DEFAULT, aClass).declaration()));
   }

   @Test
   void type()
   {
      ConsistencyTest.compileTime(context -> context.getClassOrThrow("java.lang.Object"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("java.lang.Object")))
                     .test(aClass -> assertEquals("Object", render(DEFAULT, aClass).type()));

      ConsistencyTest.compileTime(context -> context.getClassOrThrow("InterpolateGenericsExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("InterpolateGenericsExample")))
                     .withCode("InterpolateGenericsExample.java",
                               "public class InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {\n" +
                               "     static class IndependentGeneric<C> {}\n" +
                               "     static class DependentGeneric<D extends E, E> {}\n" +
                               "  }\n")
                     .test(aClass -> assertEquals("InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>",
                                                  render(DEFAULT, aClass).type()),
                           aClass -> assertEquals("InterpolateGenericsExample",
                                                  render(DEFAULT, aClass).type()));
   }
}