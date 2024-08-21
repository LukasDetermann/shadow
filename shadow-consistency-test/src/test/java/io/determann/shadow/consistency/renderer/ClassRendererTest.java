package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.<C_Class>compileTime(context -> context.getClassOrThrow("java.lang.Object"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("java.lang.Object")))
                     .test(aClass ->
                           {
                              assertEquals("public class Object {}\n", render(DEFAULT, aClass).declaration());
                              assertEquals("public class Object {\ntest\n}\n", render(DEFAULT, aClass).declaration("test"));
                           });

      ConsistencyTest.<C_Class>compileTime(context -> context.getClassOrThrow("InterpolateGenericsExample"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("InterpolateGenericsExample")))
                     .withCode("InterpolateGenericsExample.java",
                               "public class InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {}")
                     .test(aClass -> assertEquals(
                           "public class InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {}\n",
                           render(DEFAULT, aClass).declaration()));

      ConsistencyTest.<C_Class>compileTime(context -> context.getClassOrThrow("ClassParent"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("ClassParent")))
                     .withCode("ClassParent.java", "@TestAnnotation\nabstract class ClassParent extends Number {}")
                     .withCode("TestAnnotation.java", "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface TestAnnotation{}")
                     .test(aClass -> assertEquals("@TestAnnotation\nabstract class ClassParent extends Number {}\n",
                                                  render(DEFAULT, aClass).declaration()));

      ConsistencyTest.<C_Class>compileTime(context -> context.getClassOrThrow("ClassMixedParent"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("ClassMixedParent")))
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
      ConsistencyTest.<C_Class>compileTime(context -> context.getClassOrThrow("java.lang.Object"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("java.lang.Object")))
                     .test(aClass -> assertEquals("Object", render(DEFAULT, aClass).type()));

      ConsistencyTest.<C_Class>compileTime(context -> context.getClassOrThrow("InterpolateGenericsExample"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("InterpolateGenericsExample")))
                     .withCode("InterpolateGenericsExample.java", """
                         public class InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {
                              static class IndependentGeneric<C> {}
                              static class DependentGeneric<D extends E, E> {}
                           }
                          """)
                     .test(aClass -> assertEquals("InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>",
                                                  render(DEFAULT, aClass).type()),
                           aClass -> assertEquals("InterpolateGenericsExample",
                                                  render(DEFAULT, aClass).type()));
   }
}