package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.type.C_Class;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.GET_CLASS;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.test;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassRendererTest
{
   @Test
   void emptyDeclaration()
   {
      test(implementation ->
           {
              C_Class cClass = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");

              assertEquals("public class Object {}\n", render(DEFAULT, cClass).declaration());
           });
   }

   @Test
   void declaration()
   {
      test(implementation ->
           {
              C_Class cClass = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");

              assertEquals("public class Object {\ntest\n}\n", render(DEFAULT, cClass).declaration("test"));
           });
   }

   @Test
   void declarationWithDependentGenerics()
   {
      String expected = "public class InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {}\n";

      withSource("InterpolateGenericsExample.java",
                 "public class InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {}")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "InterpolateGenericsExample");

                     assertEquals(expected, render(DEFAULT, cClass).declaration());
                  });
   }

   @Test
   void declarationAnnotated()
   {
      String expected = "@TestAnnotation\nabstract class ClassParent extends Number {}\n";

      withSource("ClassParent.java",
                 "@TestAnnotation\nabstract class ClassParent extends Number {}")
            .withSource("TestAnnotation.java",
                        "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface TestAnnotation{}")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "ClassParent");

                     assertEquals(expected, render(DEFAULT, cClass).declaration());
                  });
   }

   @Test
   void declarationMultipleInheritance()
   {
      //@formatter:off
      String expected = "abstract class ClassMixedParent extends Number implements Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}\n";
      //@formatter:on
      withSource("ClassMixedParent.java",
                 "abstract class ClassMixedParent extends Number implements java.lang.Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}")
            .withSource("TestAnnotation.java",
                        "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface TestAnnotation{}")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "ClassMixedParent");

                     assertEquals(expected, render(DEFAULT, cClass).declaration());
                  });
   }

   @Test
   void type()
   {
      test(implementation ->
           {
              C_Class cClass = requestOrThrow(implementation, GET_CLASS, "java.lang.Object");
              String actual = render(DEFAULT, cClass).type();

              assertEquals("Object", actual);
           });
   }

   @Test
   void typeWithDependentGenerics()
   {
      withSource("InterpolateGenericsExample.java", """
            public class InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {
                 static class IndependentGeneric<C> {}
                 static class DependentGeneric<D extends E, E> {}
              }
            """)
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "InterpolateGenericsExample");
                     String actual = render(DEFAULT, cClass).type();

                     assertEquals("InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>", actual);
                  });
   }
}