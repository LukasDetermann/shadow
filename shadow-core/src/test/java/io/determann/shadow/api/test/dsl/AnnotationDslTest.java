package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationDslTest
{
   @Test
   void javadoc()
   {
      assertEquals("""
                   /// some java doc
                   @interface MyInterface {
                   }""",
                   Dsl.innerAnnotation().javadoc("/// some java doc")
                      .name("MyInterface")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void annotate()
   {
      assertEquals("""
                   @MyAnnotation
                   @MyAnnotation
                   @interface MyInterface {
                   }""",
                   Dsl.innerAnnotation()
                      .annotate("MyAnnotation")
                      .annotate(Dsl.annotationUsage().type("MyAnnotation"))
                      .name("MyInterface")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void modifier()
   {
      assertEquals("""
                   public dings abstract public protected private sealed non-sealed strictfp @interface MyInterface {
                   }""",
                   Dsl.innerAnnotation()
                      .modifier(Modifier.PUBLIC)
                      .modifier("dings")
                      .abstract_()
                      .public_()
                      .protected_()
                      .private_()
                      .sealed()
                      .nonSealed()
                      .strictfp_()
                      .name("MyInterface")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void body()
   {
      assertEquals("""
                   @interface MyInterface {
                   // some content
                   }""",
                   Dsl.innerAnnotation()
                      .name("MyInterface")
                      .body("// some content")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void field()
   {
      assertEquals("""
                   @interface MyInterface {
                   String s;
                   private int i;
                   }""",
                   Dsl.innerAnnotation()
                      .name("MyInterface")
                      .field("String s;")
                      .field(Dsl.field(Modifier.PRIVATE, "int", "i"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void method()
   {
      assertEquals("""
                   @interface MyInterface {
                   String foo();
                   String myMethod();
                   
                   }""",
                   Dsl.innerAnnotation()
                      .name("MyInterface")
                      .method("String foo();")
                      .method(Dsl.method().result("String").name("myMethod"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void inner()
   {
      assertEquals("""
                   @interface MyInterface {
                   @interface Inner {}
                   @interface Inner2 {
                   }
                   
                   }""",
                   Dsl.innerAnnotation()
                      .name("MyInterface")
                      .inner("@interface Inner {}")
                      .inner(Dsl.innerAnnotation().name("Inner2"))
                      .renderDeclaration(RenderingContext.DEFAULT));
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
                   Dsl.annotation()
                      .copyright("// some copyright")
                      .package_(Dsl.packageInfo().name("org.example"))
                         .javadoc("/// some javadoc")
                      .name("MyInterface")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void package_()
   {
      assertEquals("""
                   package org.example;
                   
                   @interface MyInterface {
                   }""",
                   Dsl.annotation()
                      .package_("org.example")
                      .name("MyInterface")
                      .renderDeclaration(RenderingContext.DEFAULT));
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
                   import static MyInterface2;
                   import static some.other.package.*;
                   
                   @interface MyInterface {
                   }""",
                   Dsl.annotation()
                      .package_("org.example")
                      .import_("some.thing")
                      .import_(Dsl.import_("Dings"))
                      .import_(Dsl.importAll(Dsl.packageInfo().name("other.package")))
                      .import_(Dsl.staticImport("foo.package"))
                      .import_(Dsl.staticImport(Dsl.innerAnnotation().name("MyInterface2")))
                      .import_(Dsl.staticImportAll(Dsl.packageInfo().name("some.other.package")))
                      .name("MyInterface")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void renderQualifiedName()
   {
      assertEquals("org.example.MyAnnotation",
                   Dsl.annotation()
                      .package_("org.example")
                      .name("MyAnnotation")
                      .renderQualifiedName(RenderingContext.DEFAULT));
   }

   @Test
   void renderType()
   {
      assertEquals("org.example.MyAnnotation",
                   Dsl.annotation()
                      .package_("org.example")
                      .name("MyAnnotation")
                      .renderType(RenderingContext.DEFAULT));
   }

   @Test
   void renderName()
   {
      assertEquals("MyAnnotation",
                   Dsl.annotation()
                      .package_("org.example")
                      .name("MyAnnotation")
                      .renderName(RenderingContext.DEFAULT));
   }

   @Test
   void renderMethod()
   {
      assertEquals("""
                   @interface MyAnnotation {
                   String foo() default "";
                   
                   }""",
                   Dsl.innerAnnotation()
                      .name("MyAnnotation")
                      .method(Dsl.method().result("String").name("foo"),
                              Dsl.annotationValue(""))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }
}
