package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.class_.ClassRenderable;
import io.determann.shadow.api.dsl.constructor.ConstructorRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;
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
                   class MyClass {
                      void method(@Test @MyAnnotation MyClass MyClass.this, String s) {}
                   
                   }""",
                   Dsl.innerClass()
                      .name("MyClass")
                      .method(method)
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void methodThrows()
   {
      MethodRenderable methodRenderable = Dsl.method().result("void").name("method").receiver(Dsl.receiver());

      assertThrows(IllegalStateException.class, () -> methodRenderable.renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void constructor()
   {
      ClassRenderable outerClass = Dsl.innerClass().name("Outer")
                                      .inner(Dsl.innerClass().name("Inner")
                                                .constructor(Dsl.constructor().type("Inner")
                                                                .receiver(Dsl.receiver())));

      assertEquals("""
                   class Outer {
                      class Inner {
                         Inner(Outer Outer.this) {}
                   
                      }
                   
                   }""",
                   outerClass.renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void constructorMissingOuterClass()
   {
      ClassRenderable renderable = Dsl.innerClass().name("Inner")
                                      .constructor(Dsl.constructor().type("Inner")
                                                      .receiver(Dsl.receiver()));

      assertThrows(IllegalStateException.class, () -> renderable.renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void constructorMissingInnerClass()
   {
      ConstructorRenderable renderable = Dsl.constructor().type("Inner")
                                            .receiver(Dsl.receiver());

      assertThrows(IllegalStateException.class, () -> renderable.renderDeclaration(RenderingContext.DEFAULT));
   }
}
