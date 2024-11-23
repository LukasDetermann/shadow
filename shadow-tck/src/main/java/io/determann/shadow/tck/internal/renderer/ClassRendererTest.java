package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.tck.internal.RenderingTestBuilder;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.RenderingTestBuilder.renderingTest;

class ClassRendererTest
{
   @Test
   void declaration()
   {
      RenderingTestBuilder<C_Class> objectRenderingTest = renderingTest(C_Class.class).withToRender("java.lang.Object");

      objectRenderingTest.withRender(aClass -> render(DEFAULT, aClass).declaration())
                         .withExpected("public class Object {}\n")
                         .test();


      objectRenderingTest.withRender(aClass -> render(DEFAULT, aClass).declaration("test"))
                         .withExpected("public class Object {\ntest\n}\n")
                         .test();

      renderingTest(C_Class.class).withSource("InterpolateGenericsExample.java",
                                              "public class InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {}")
                                  .withToRender("InterpolateGenericsExample")
                                  .withRender(aClass -> render(DEFAULT, aClass).declaration())
                                  .withExpected("public class InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {}\n")
                                  .test();

      renderingTest(C_Class.class).withSource("ClassParent.java",
                                              "@TestAnnotation\nabstract class ClassParent extends Number {}")
                                  .withSource("TestAnnotation.java",
                                              "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface TestAnnotation{}")
                                  .withToRender("ClassParent")
                                  .withRender(aClass -> render(DEFAULT, aClass).declaration())
                                  .withExpected("@TestAnnotation\nabstract class ClassParent extends Number {}\n")
                                  .test();

      renderingTest(C_Class.class).withSource("ClassMixedParent.java",
                                              "abstract class ClassMixedParent extends Number implements java.lang.Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}")
                                  .withSource("TestAnnotation.java",
                                              "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface TestAnnotation{}")
                                  .withToRender("ClassMixedParent")
                                  .withRender(cClass -> render(DEFAULT, cClass).declaration())
                                  .withExpected(
                                        "abstract class ClassMixedParent extends Number implements Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}\n")
                                  .test();
   }

   @Test
   void type()
   {
      renderingTest(C_Class.class).withToRender("java.lang.Object")
                                  .withRender(cClass -> render(DEFAULT, cClass).type())
                                  .withExpected("Object")
                                  .test();

      renderingTest(C_Class.class).withSource("InterpolateGenericsExample.java", """
                         public class InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {
                              static class IndependentGeneric<C> {}
                              static class DependentGeneric<D extends E, E> {}
                           }
                          """)
            .withToRender("InterpolateGenericsExample")
            .withRender(cClass -> render(DEFAULT, cClass).type())
            .withExpected("InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>")
            .test();
   }
}