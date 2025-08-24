package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumDslTest
{
   @Test
   void javadoc()
   {
      assertEquals("""
                   /// some java doc
                   enum MyEnum {
                   }""",
                   Dsl.innerEnum().javadoc("/// some java doc")
                      .name("MyEnum")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void annotate()
   {
      assertEquals("""
                   @MyAnnotation
                   @MyAnnotation
                   enum MyEnum {
                   }""",
                   Dsl.innerEnum()
                      .annotate("MyAnnotation")
                      .annotate(Dsl.annotationUsage().type("MyAnnotation"))
                      .name("MyEnum")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void modifier()
   {
      assertEquals("""
                   public dings public protected private static strictfp enum MyEnum {
                   }""",
                   Dsl.innerEnum()
                      .modifier(Modifier.PUBLIC)
                      .modifier("dings")
                      .public_()
                      .protected_()
                      .private_()
                      .static_()
                      .strictfp_()
                      .name("MyEnum")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void implements_()
   {
      assertEquals("""
                   enum MyEnum implements SomeInterface, Another {
                   }""",
                   Dsl.innerEnum()
                      .name("MyEnum")
                      .implements_("SomeInterface")
                      .implements_(Dsl.innerInterface().name("Another"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }


   @Test
   void body()
   {
      assertEquals("""
                   enum MyEnum {
                      // some content
                   }""",
                   Dsl.innerEnum()
                      .name("MyEnum")
                      .body("// some content")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void field()
   {
      assertEquals("""
                   enum MyEnum {
                      String s;
                      private int i;
                   }""",
                   Dsl.innerEnum()
                      .name("MyEnum")
                      .field("String s;")
                      .field(Dsl.field(Modifier.PRIVATE, "int", "i"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void method()
   {
      assertEquals("""
                   enum MyEnum {
                      abstract void foo() {}
                      String myMethod() {}
                   
                   }""",
                   Dsl.innerEnum()
                      .name("MyEnum")
                      .method("abstract void foo() {}")
                      .method(Dsl.method().result("String").name("myMethod"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void inner()
   {
      assertEquals("""
                   enum MyEnum {
                      enum Inner {}
                      enum Inner2 {
                      }
                   
                   }""",
                   Dsl.innerEnum()
                      .name("MyEnum")
                      .inner("enum Inner {}")
                      .inner(Dsl.innerEnum().name("Inner2"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void instanceInitializer()
   {
      assertEquals("""
                   enum MyEnum {
                      {
                      // something
                      }
                   
                   }""",
                   Dsl.innerEnum()
                      .name("MyEnum")
                      .instanceInitializer("""
                                           {
                                           // something
                                           }""")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void staticInitializer()
   {
      assertEquals("""
                   enum MyEnum {
                      static {
                      // something
                      }
                   
                   }""",
                   Dsl.innerEnum()
                      .name("MyEnum")
                      .staticInitializer("""
                                         static {
                                         // something
                                         }""")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void constructor()
   {
      assertEquals("""
                   enum MyEnum {
                      MyEnum() {}
                      MyEnum2() {}
                   
                   }""",
                   Dsl.innerEnum()
                      .name("MyEnum")
                      .constructor("MyEnum() {}")
                      .constructor(Dsl.constructor().type("MyEnum2"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void copyright()
   {
      assertEquals("""
                   // some copyright
                   package org.example;
                   
                   /// some javadoc
                   enum MyEnum {
                   }""",
                   Dsl.enum_()
                      .copyright("// some copyright")
                      .package_(Dsl.packageInfo().name("org.example"))
                      .javadoc("/// some javadoc")
                      .name("MyEnum")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void package_()
   {
      assertEquals("""
                   package org.example;
                   
                   enum MyEnum {
                   }""",
                   Dsl.enum_()
                      .package_("org.example")
                      .name("MyEnum")
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
                   
                   enum MyEnum {
                   }""",
                   Dsl.enum_()
                      .package_("org.example")
                      .import_("some.thing")
                      .import_(Dsl.import_("Dings"))
                      .import_(Dsl.importAll(Dsl.packageInfo().name("other.package")))
                      .import_(Dsl.staticImport("foo.package"))
                      .import_(Dsl.staticImport(Dsl.innerAnnotation().name("MyInterface2")))
                      .import_(Dsl.staticImportAll(Dsl.packageInfo().name("some.other.package")))
                      .name("MyEnum")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void renderQualifiedName()
   {
      assertEquals("MyEnum",
                   Dsl.innerEnum()
                      .name("MyEnum")
                      .renderQualifiedName(RenderingContext.DEFAULT));

      assertEquals("org.example.MyEnum",
                   Dsl.enum_()
                      .package_("org.example")
                      .name("MyEnum")
                      .renderQualifiedName(RenderingContext.DEFAULT));
   }

   @Test
   void renderType()
   {
      assertEquals("org.example.MyEnum",
                   Dsl.enum_()
                      .package_("org.example")
                      .name("MyEnum")
                      .renderType(RenderingContext.DEFAULT));
   }

   @Test
   void renderName()
   {
      assertEquals("MyEnum",
                   Dsl.innerEnum()
                      .name("MyEnum")
                      .renderName(RenderingContext.DEFAULT));
   }

   @Test
   void enumConstant()
   {
      assertEquals("""
                   enum MyEnum {
                      T1,
                      T2;
                   }""",
                   Dsl.innerEnum()
                      .name("MyEnum")
                      .enumConstant("T1")
                      .enumConstant(Dsl.enumConstant().name("T2"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }
}
