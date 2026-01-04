package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.Dsl;
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
      MethodRenderable method = Dsl.method()
                                   .result("void")
                                   .name("method")
                                   .receiver(Dsl.receiver()
                                                .annotate("Test")
                                                .annotate(Dsl.annotationUsage()
                                                             .type("MyAnnotation")))
                                   .parameter(Dsl.parameter("String", "s"));

      assertEquals("""
                   package org.example;
                   
                   class MyClass {
                      void method(@Test @MyAnnotation MyClass MyClass.this, String s) {}
                   
                   }""",
                   Dsl.class_()
                      .package_( "org.example")
                      .name("MyClass")
                      .method(method)
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void methodThrows()
   {
      MethodRenderable methodRenderable = Dsl.method().result("void").name("method").receiver(Dsl.receiver());

      assertThrows(IllegalStateException.class, () -> methodRenderable.renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void constructor()
   {
      ClassRenderable outerClass = Dsl.class_().package_("org.example")
                                      .name("Outer")
                                      .inner(Dsl.innerClass().outer("Outer").name("Inner")
                                                .constructor(Dsl.constructor().type("Inner")
                                                                .receiver(Dsl.receiver())));

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
      ClassRenderable renderable = Dsl.innerClass().outer("Outer")
                                      .name("Inner")
                                      .constructor(Dsl.constructor().type("Inner")
                                                      .receiver(Dsl.receiver()));

      assertThrows(IllegalStateException.class, () -> renderable.renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void constructorMissingInnerClass()
   {
      ConstructorRenderable renderable = Dsl.constructor().type("Inner")
                                            .receiver(Dsl.receiver());

      assertThrows(IllegalStateException.class, () -> renderable.renderDeclaration(RenderingContext.createRenderingContext()));
   }
}
