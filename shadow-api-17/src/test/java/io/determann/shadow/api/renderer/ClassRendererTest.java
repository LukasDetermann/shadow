package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassRendererTest
{

   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Class aClass = shadowApi.getClassOrThrow("java.lang.Object");
                               assertEquals("public class Object {}\n", render(DEFAULT, aClass).declaration());
                               assertEquals("public class Object {\ntest\n}\n", render(DEFAULT, aClass).declaration("test"));

                               assertEquals("public class InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {}\n",
                                            render(DEFAULT, shadowApi.getClassOrThrow("InterpolateGenericsExample")).declaration());

                               assertEquals("@TestAnnotation\nabstract class ClassParent extends Number {}\n",
                                            render(DEFAULT, shadowApi.getClassOrThrow("ClassParent")).declaration());

                               assertEquals(
                                     "abstract class ClassMixedParent extends Number implements Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}\n",
                                     render(DEFAULT, shadowApi.getClassOrThrow("ClassMixedParent")).declaration());
                            })
                   .withCodeToCompile("InterpolateGenericsExample.java", """
                         public class InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {
                              static class IndependentGeneric<C> {}
                              static class DependentGeneric<D extends E, E> {}
                           }
                          """)
                   .withCodeToCompile("ClassParent.java", "@TestAnnotation\nabstract class ClassParent extends Number {}")
                   .withCodeToCompile("ClassMixedParent.java",
                                      "abstract class ClassMixedParent extends Number implements java.lang.Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}")
                   .withCodeToCompile("TestAnnotation.java", "@interface TestAnnotation{}")
                   .compile();
   }

   @Test
   void type()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Class aClass = shadowApi.getClassOrThrow("java.lang.Object");
                               assertEquals("Object", render(DEFAULT, aClass).type());

                               Class generics = shadowApi.getClassOrThrow("InterpolateGenericsExample");
                               assertEquals("InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>",
                                            render(DEFAULT, generics).type());
                            })
                   .withCodeToCompile("InterpolateGenericsExample.java", """
                         public class InterpolateGenericsExample <A extends Comparable<B>, B extends Comparable<A>> {
                              static class IndependentGeneric<C> {}
                              static class DependentGeneric<D extends E, E> {}
                           }
                          """)
                   .compile();
   }
}