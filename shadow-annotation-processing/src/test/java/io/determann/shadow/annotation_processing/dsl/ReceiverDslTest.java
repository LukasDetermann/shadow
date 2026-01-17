package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.JavaDsl;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import io.determann.shadow.api.annotation_processing.dsl.class_.ClassRenderable;
import io.determann.shadow.api.annotation_processing.dsl.constructor.ConstructorRenderable;
import io.determann.shadow.api.annotation_processing.dsl.method.MethodRenderable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReceiverDslTest
{
   @Test
   void method()
   {
      MethodRenderable method = JavaDsl.method()
                                       .result("void")
                                       .name("method")
                                       .receiver(JavaDsl.receiver()
                                                        .annotate("Test")
                                                        .annotate(JavaDsl.annotationUsage()
                                                                         .type("MyAnnotation")))
                                       .parameter(JavaDsl.parameter("String", "s"));

      assertEquals("""
                   package org.example;
                   
                   class MyClass {
                      void method(@Test @MyAnnotation MyClass MyClass.this, String s) {}
                   
                   }""",
                   JavaDsl.class_()
                          .package_( "org.example")
                          .name("MyClass")
                          .method(method)
                          .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void methodThrows()
   {
      MethodRenderable methodRenderable = JavaDsl.method().result("void").name("method").receiver(JavaDsl.receiver());

      assertThrows(IllegalStateException.class, () -> methodRenderable.renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void constructor()
   {
      ClassRenderable outerClass = JavaDsl.class_().package_("org.example")
                                          .name("Outer")
                                          .inner(JavaDsl.innerClass().outer("Outer").name("Inner")
                                                        .constructor(JavaDsl.constructor().type("Inner")
                                                                            .receiver(JavaDsl.receiver())));

      assertEquals("""
                   package org.example;
                   
                   class Outer {
                      class Inner {
                         Inner(Outer Outer.this) {}
                   
                      }
                   
                   }""",
                   outerClass.renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void constructorMissingOuterClass()
   {
      ClassRenderable renderable = JavaDsl.innerClass().outer("Outer")
                                          .name("Inner")
                                          .constructor(JavaDsl.constructor().type("Inner")
                                                              .receiver(JavaDsl.receiver()));

      assertThrows(IllegalStateException.class, () -> renderable.renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void constructorMissingInnerClass()
   {
      ConstructorRenderable renderable = JavaDsl.constructor().type("Inner")
                                                .receiver(JavaDsl.receiver());

      assertThrows(IllegalStateException.class, () -> renderable.renderDeclaration(RenderingContext.createRenderingContext()));
   }
}
