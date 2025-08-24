package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InterfaceDslTest
{
   @Test
   void javadoc()
   {
      assertEquals("""
                   /// some java doc
                   interface MyInterface {
                   }""",
                   Dsl.innerInterface().javadoc("/// some java doc")
                      .name("MyInterface")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void annotate()
   {
      assertEquals("""
                   @MyAnnotation
                   @MyAnnotation
                   interface MyInterface {
                   }""",
                   Dsl.innerInterface()
                      .annotate("MyAnnotation")
                      .annotate(Dsl.annotationUsage().type("MyAnnotation"))
                      .name("MyInterface")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void modifier()
   {
      assertEquals("""
                   public dings abstract public protected private sealed non-sealed static strictfp interface MyInterface {
                   }""",
                   Dsl.innerInterface()
                      .modifier(Modifier.PUBLIC)
                      .modifier("dings")
                      .abstract_()
                      .public_()
                      .protected_()
                      .private_()
                      .sealed()
                      .nonSealed()
                      .static_()
                      .strictfp_()
                      .name("MyInterface")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void generic()
   {
      assertEquals("""
                   interface MyInterface <T, V> {
                   }""",
                   Dsl.innerInterface()
                      .name("MyInterface")
                      .generic("T")
                      .generic(Dsl.generic("V"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void extends_()
   {
      assertEquals("""
                   interface MyInterface extends SomeInterface, Another {
                   }""",
                   Dsl.innerInterface()
                      .name("MyInterface")
                      .extends_("SomeInterface")
                      .extends_(Dsl.innerInterface().name("Another"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void permits()
   {
      assertEquals("""
                   interface MyInterface permits Some, Thing {
                   }""",
                   Dsl.innerInterface()
                      .name("MyInterface")
                      .permits("Some")
                      .permits(Dsl.innerInterface().name("Thing"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void body()
   {
      assertEquals("""
                   interface MyInterface {
                      // some content
                   }""",
                   Dsl.innerInterface()
                      .name("MyInterface")
                      .body("// some content")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void field()
   {
      assertEquals("""
                   interface MyInterface {
                      String s;
                      private int i;
                   }""",
                   Dsl.innerInterface()
                      .name("MyInterface")
                      .field("String s;")
                      .field(Dsl.field(Modifier.PRIVATE, "int", "i"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void method()
   {
      assertEquals("""
                   interface MyInterface {
                      abstract void foo() {}
                      String myMethod() {}
                   
                   }""",
                   Dsl.innerInterface()
                      .name("MyInterface")
                      .method("abstract void foo() {}")
                      .method(Dsl.method().result("String").name("myMethod"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void inner()
   {
      assertEquals("""
                   interface MyInterface {
                      interface Inner {}
                      interface Inner2 {
                      }
                   
                   }""",
                   Dsl.innerInterface()
                      .name("MyInterface")
                      .inner("interface Inner {}")
                      .inner(Dsl.innerInterface().name("Inner2"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void copyright()
   {
      assertEquals("""
                   // some copyright
                   package org.example;
                   
                   /// some javadoc
                   interface MyInterface {
                   }""",
                   Dsl.interface_()
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
                   
                   interface MyInterface {
                   }""",
                   Dsl.interface_()
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
                   
                   interface MyInterface {
                   }""",
                   Dsl.interface_()
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
   void renderName()
   {
      assertEquals("MyInterface", Dsl.innerInterface().name("MyInterface").renderName(RenderingContext.DEFAULT));
   }

   @Test
   void renderType()
   {
      assertEquals("MyInterface<T, S>",
                   Dsl.innerInterface()
                      .name("MyInterface")
                      .generic("T")
                      .generic("S")
                      .renderType(RenderingContext.DEFAULT));
   }

   @Test
   void renderQualifiedName()
   {
      assertEquals("org.example.MyInterface",
                   Dsl.interface_()
                         .package_("org.example")
                         .name("MyInterface")
                         .renderQualifiedName(RenderingContext.DEFAULT));
   }
}
