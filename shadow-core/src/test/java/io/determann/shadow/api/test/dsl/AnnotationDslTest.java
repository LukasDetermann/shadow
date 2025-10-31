package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.annotation.AnnotationImportStep;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.dsl.Dsl.annotation;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationDslTest
{

   public static final AnnotationImportStep ANNOTATION = Dsl.annotation().package_("org.example");

   @Test
   void javadoc()
   {
      assertEquals("""
                   package org.example;
                   
                   /// some java doc
                   @interface MyInterface {
                   }""",
                   ANNOTATION.javadoc("/// some java doc")
                             .name("MyInterface")
                             .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void annotate()
   {
      assertEquals("""
                   package org.example;
                   
                   @MyAnnotation
                   @MyAnnotation
                   @interface MyInterface {
                   }""",
                   ANNOTATION.annotate("MyAnnotation")
                             .annotate(Dsl.annotationUsage().type("MyAnnotation"))
                             .name("MyInterface")
                             .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void modifier()
   {
      assertEquals("""
                   package org.example;
                   
                   public dings abstract public protected private sealed non-sealed strictfp @interface MyInterface {
                   }""",
                   ANNOTATION.modifier(Modifier.PUBLIC)
                             .modifier("dings")
                             .abstract_()
                             .public_()
                             .protected_()
                             .private_()
                             .sealed()
                             .nonSealed()
                             .strictfp_()
                             .name("MyInterface")
                             .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void body()
   {
      assertEquals("""
                   package org.example;
                   
                   @interface MyInterface {
                      // some content
                   }""",
                   ANNOTATION.name("MyInterface")
                             .body("// some content")
                             .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void field()
   {
      assertEquals("""
                   package org.example;
                   
                   @interface MyInterface {
                      String s;
                      private int i;
                   }""",
                   ANNOTATION.name("MyInterface")
                             .field("String s;")
                             .field(Dsl.field().private_().type("int").name("i"))
                             .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void method()
   {
      assertEquals("""
                   package org.example;
                   
                   @interface MyInterface {
                      String foo();
                      String myMethod();
                   
                   }""",
                   ANNOTATION.name("MyInterface")
                             .method("String foo();")
                             .method(Dsl.method().result("String").name("myMethod"))
                             .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void inner()
   {
      assertEquals("""
                   package org.example;
                   
                   @interface MyInterface {
                      @interface Inner {}
                      @interface Inner2 {
                      }
                   
                   }""",
                   ANNOTATION.name("MyInterface")
                             .inner("@interface Inner {}")
                             .inner(Dsl.innerAnnotation().outer("org.example.MyInterface").name("Inner2"))
                             .renderDeclaration(RenderingContext.createRenderingContext()));
   }


   @Test
   void copyright()
   {
      assertEquals("""
                   // some copyright
                   package org.example;
                   
                   /// some javadoc
                   @interface MyInterface {
                   }""",
                   annotation()
                      .copyright("// some copyright")
                      .package_(Dsl.packageInfo().name("org.example"))
                         .javadoc("/// some javadoc")
                      .name("MyInterface")
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void package_()
   {
      assertEquals("""
                   package org.example;
                   
                   @interface MyInterface {
                   }""",
                   annotation()
                      .package_("org.example")
                      .name("MyInterface")
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void import_()
   {
      assertEquals("""
                   package org.example;
                   
                   import some.thing;
                   import Dings;
                   import other.package.*;
                   import static foo.package;
                   import static org.example.MyInterface2;
                   import static some.other.package.*;
                   
                   @interface MyInterface {
                   }""",
                   annotation()
                      .package_("org.example")
                      .import_("some.thing")
                      .import_(Dsl.import_("Dings"))
                      .import_(Dsl.import_().importAll(Dsl.packageInfo().name("other.package")))
                      .import_(Dsl.import_().static_().import_("foo.package"))
                      .import_(Dsl.import_().static_().import_(ANNOTATION.name("MyInterface2")))
                      .import_(Dsl.import_().static_().importAll(Dsl.packageInfo().name("some.other.package")))
                      .name("MyInterface")
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void renderQualifiedName()
   {
      assertEquals("org.example.MyAnnotation",
                   annotation()
                      .package_("org.example")
                      .name("MyAnnotation")
                      .renderQualifiedName(RenderingContext.createRenderingContext()));
   }

   @Test
   void renderType()
   {
      assertEquals("org.example.MyAnnotation",
                   annotation()
                      .package_("org.example")
                      .name("MyAnnotation")
                      .renderType(RenderingContext.createRenderingContext()));
   }

   @Test
   void renderName()
   {
      assertEquals("MyAnnotation",
                   annotation()
                      .package_("org.example")
                      .name("MyAnnotation")
                      .renderName(RenderingContext.createRenderingContext()));
   }

   @Test
   void renderMethod()
   {
      assertEquals("""
                   package org.example;
                   
                   @interface MyAnnotation {
                      String foo() default "";
                   
                   }""",
                   ANNOTATION.name("MyAnnotation")
                             .method(Dsl.method().result("String").name("foo"),
                                     Dsl.annotationValue(""))
                             .renderDeclaration(RenderingContext.createRenderingContext()));
   }
}
