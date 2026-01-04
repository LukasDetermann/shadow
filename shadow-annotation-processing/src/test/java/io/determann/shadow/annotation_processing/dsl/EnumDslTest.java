package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.Modifier;
import io.determann.shadow.api.annotation_processing.dsl.Dsl;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import io.determann.shadow.api.annotation_processing.dsl.enum_.EnumImportStep;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumDslTest
{

   public static final EnumImportStep ENUM = Dsl.enum_().package_("org.example");

   @Test
   void javadoc()
   {
      assertEquals("""
                   package org.example;
                   
                   /// some java doc
                   enum MyEnum {
                   }""",
                   ENUM.javadoc("/// some java doc")
                       .name("MyEnum")
                       .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void annotate()
   {
      assertEquals("""
                   package org.example;
                   
                   @MyAnnotation
                   @MyAnnotation
                   enum MyEnum {
                   }""",
                   ENUM.annotate("MyAnnotation")
                       .annotate(Dsl.annotationUsage().type("MyAnnotation"))
                       .name("MyEnum")
                       .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void modifier()
   {
      assertEquals("""
                   package org.example;
                   
                   public dings public protected private static strictfp enum MyEnum {
                   }""",
                   ENUM.modifier(Modifier.PUBLIC)
                       .modifier("dings")
                       .public_()
                       .protected_()
                       .private_()
                       .static_()
                       .strictfp_()
                       .name("MyEnum")
                       .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void implements_()
   {
      assertEquals("""
                   package org.example;
                   
                   enum MyEnum implements SomeInterface, Another {
                   }""",
                   ENUM.name("MyEnum")
                       .implements_("SomeInterface")
                       .implements_(Dsl.interface_().package_("org.example").name("Another"))
                       .renderDeclaration(RenderingContext.createRenderingContext()));
   }


   @Test
   void body()
   {
      assertEquals("""
                   package org.example;
                   
                   enum MyEnum {
                      // some content
                   }""",
                   ENUM.name("MyEnum")
                       .body("// some content")
                       .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void field()
   {
      assertEquals("""
                   package org.example;
                   
                   enum MyEnum {
                      String s;
                      private int i;
                   }""",
                   ENUM.name("MyEnum")
                       .field("String s;")
                       .field(Dsl.field().private_().type("int").name("i"))
                       .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void method()
   {
      assertEquals("""
                   package org.example;
                   
                   enum MyEnum {
                      abstract void foo() {}
                      String myMethod() {}
                   
                   }""",
                   ENUM.name("MyEnum")
                       .method("abstract void foo() {}")
                       .method(Dsl.method().result("String").name("myMethod"))
                       .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void inner()
   {
      assertEquals("""
                   package org.example;
                   
                   enum MyEnum {
                      enum Inner {}
                      enum Inner2 {
                      }
                   
                   }""",
                   ENUM.name("MyEnum")
                       .inner("enum Inner {}")
                       .inner(Dsl.innerEnum().outer("org.example.MyEnum").name("Inner2"))
                       .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void instanceInitializer()
   {
      assertEquals("""
                   package org.example;
                   
                   enum MyEnum {
                      {
                      // something
                      }
                   
                   }""",
                   ENUM.name("MyEnum")
                       .instanceInitializer("""
                                            {
                                            // something
                                            }""")
                       .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void staticInitializer()
   {
      assertEquals("""
                   package org.example;
                   
                   enum MyEnum {
                      static {
                      // something
                      }
                   
                   }""",
                   ENUM.name("MyEnum")
                       .staticInitializer("""
                                          static {
                                          // something
                                          }""")
                       .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void constructor()
   {
      assertEquals("""
                   package org.example;
                   
                   enum MyEnum {
                      MyEnum() {}
                      MyEnum2() {}
                   
                   }""",
                   ENUM.name("MyEnum")
                       .constructor("MyEnum() {}")
                       .constructor(Dsl.constructor().type("MyEnum2"))
                       .renderDeclaration(RenderingContext.createRenderingContext()));
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
                      .renderDeclaration(RenderingContext.createRenderingContext()));
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
                   import static foo.package.MyType.*;
                   import static org.example.MyInterface2.*;
                   
                   enum MyEnum {
                   }""",
                   Dsl.enum_()
                      .package_("org.example")
                      .import_("some.thing")
                      .import_(Dsl.import_("Dings"))
                      .import_(Dsl.import_().package_(Dsl.packageInfo().name("other.package")))
                      .import_(Dsl.import_().static_().declared("foo.package.MyType"))
                      .import_(Dsl.import_().static_().declared(Dsl.annotation().package_("org.example").name("MyInterface2")))
                      .name("MyEnum")
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void renderQualifiedName()
   {
      assertEquals("org.example.MyEnum",
                   ENUM.name("MyEnum")
                       .renderQualifiedName(RenderingContext.createRenderingContext()));

      assertEquals("org.example.MyEnum",
                   Dsl.enum_()
                      .package_("org.example")
                      .name("MyEnum")
                      .renderQualifiedName(RenderingContext.createRenderingContext()));
   }

   @Test
   void renderType()
   {
      assertEquals("MyEnum",
                   Dsl.enum_()
                      .package_("org.example")
                      .name("MyEnum")
                      .renderType(RenderingContext.createRenderingContext()));
   }

   @Test
   void renderName()
   {
      assertEquals("MyEnum",
                   ENUM.name("MyEnum")
                       .renderName(RenderingContext.createRenderingContext()));
   }

   @Test
   void enumConstant()
   {
      assertEquals("""
                   package org.example;
                   
                   enum MyEnum {
                      T1,
                      T2;
                   }""",
                   ENUM.name("MyEnum")
                       .enumConstant("T1")
                       .enumConstant(Dsl.enumConstant().name("T2"))
                       .renderDeclaration(RenderingContext.createRenderingContext()));
   }
}
