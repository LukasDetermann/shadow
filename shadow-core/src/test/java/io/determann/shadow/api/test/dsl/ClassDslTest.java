package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.class_.ClassImportStep;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassDslTest
{
   private static final ClassImportStep CLASS = Dsl.class_().package_("org.example");

   @Test
   void javadoc()
   {
      assertEquals("""
                   package org.example;
                   
                   /// some java doc
                   class MyClass {
                   }""",
                   CLASS.javadoc("/// some java doc")
                        .name("MyClass")
                        .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void annotate()
   {
      assertEquals("""
                   package org.example;
                   
                   @MyAnnotation
                   @MyAnnotation
                   class MyClass {
                   }""",
                   CLASS.annotate("MyAnnotation")
                      .annotate(Dsl.annotationUsage().type("MyAnnotation"))
                      .name("MyClass")
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void modifier()
   {
      assertEquals("""
                   package org.example;
                   
                   public dings abstract public protected private final sealed non-sealed static strictfp class MyClass {
                   }""",
                   CLASS.modifier(Modifier.PUBLIC)
                      .modifier("dings")
                      .abstract_()
                      .public_()
                      .protected_()
                      .private_()
                      .final_()
                      .sealed()
                      .nonSealed()
                      .static_()
                      .strictfp_()
                      .name("MyClass")
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void genericDeclaration()
   {
      assertEquals("""
                   package org.example;
                   
                   class MyClass <T, V> {
                   }""",
                   CLASS.name("MyClass")
                      .genericDeclaration("T")
                      .genericDeclaration(Dsl.generic("V"))
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void extends_()
   {
      assertEquals("""
                   package org.example;
                   
                   class MyClass extends SomeOther {
                   }""",
                   CLASS.name("MyClass")
                        .extends_("SomeOther")
                        .renderDeclaration(RenderingContext.createRenderingContext()));

      assertEquals("""
                   package org.example;
                   
                   class MyClass extends Parent {
                   }""",
                   Dsl.class_()
                      .package_("org.example")
                      .name("MyClass")
                      .extends_(Dsl.class_().package_("org.example").name("Parent"))
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void implements_()
   {
      assertEquals("""
                   package org.example;
                   
                   class MyClass implements SomeInterface, Another {
                   }""",
                   CLASS.name("MyClass")
                        .implements_("SomeInterface")
                        .implements_(Dsl.interface_().package_("org.example").name("Another"))
                        .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void permits()
   {
      assertEquals("""
                   package org.example;
                   
                   class MyClass permits Some, Thing {
                   }""",
                   CLASS.name("MyClass")
                        .permits("Some")
                        .permits(CLASS.name("Thing"))
                        .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void body()
   {
      assertEquals("""
                   package org.example;
                   
                   class MyClass {
                      // some content
                   }""",
                   CLASS.name("MyClass")
                        .body("// some content")
                        .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void field()
   {
      assertEquals("""
                   package org.example;
                   
                   class MyClass {
                      String s;
                      private int i;
                   }""",
                   CLASS.name("MyClass")
                        .field("String s;")
                        .field(Dsl.field().private_().type("int").name("i"))
                        .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void method()
   {
      assertEquals("""
                   package org.example;
                   
                   class MyClass {
                      abstract void foo() {}
                      String myMethod() {}
                   
                   }""",
                   CLASS.name("MyClass")
                        .method("abstract void foo() {}")
                        .method(Dsl.method().result("String").name("myMethod"))
                        .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void inner()
   {
      assertEquals("""
                   package org.example;
                   
                   class MyClass {
                      class Inner {}
                      class Inner2 {
                      }
                   
                   }""",
                   CLASS.name("MyClass")
                        .inner("class Inner {}")
                        .inner(Dsl.innerClass().outer("Outer").name("Inner2"))
                        .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void instanceInitializer()
   {
      assertEquals("""
                   package org.example;
                   
                   class MyClass {
                      {
                      // something
                      }
                   
                   }""",
                   CLASS.name("MyClass")
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
                   
                   class MyClass {
                      static {
                      // something
                      }
                   
                   }""",
                   CLASS.name("MyClass")
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
                   
                   class MyClass {
                      MyClass() {}
                      MyClass2() {}
                   
                   }""",
                   CLASS.name("MyClass")
                      .constructor("MyClass() {}")
                      .constructor(Dsl.constructor().type("MyClass2"))
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void copyright()
   {
      assertEquals("""
                   // some copyright
                   package org.example;
                   
                   /// some javadoc
                   class MyClass {
                   }""",
                   Dsl.class_()
                      .copyright("// some copyright")
                      .package_(Dsl.packageInfo().name("org.example"))
                      .javadoc("/// some javadoc")
                      .name("MyClass")
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void package_()
   {
      assertEquals("""
                   package org.example;
                   
                   class MyClass {
                   }""",
                   CLASS.name("MyClass")
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
                   
                   class MyClass {
                   }""",
                   CLASS.import_("some.thing")
                        .import_(Dsl.import_("Dings"))
                        .import_(Dsl.import_().importAll(Dsl.packageInfo().name("other.package")))
                        .import_(Dsl.import_().static_().import_("foo.package"))
                        .import_(Dsl.import_().static_().import_(Dsl.annotation().package_("org.example").name("MyInterface2")))
                        .import_(Dsl.import_().static_().importAll(Dsl.packageInfo().name("some.other.package")))
                        .name("MyClass")
                        .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void renderType()
   {
      assertEquals("MyClass<T, S>",
                   CLASS.name("MyClass")
                        .genericUsage("T")
                        .genericUsage("S")
                        .renderType(RenderingContext.createRenderingContext()));
   }

   @Test
   void renderQualifiedName()
   {
      assertEquals("org.example.MyClass",
                   CLASS.name("MyClass")
                        .renderQualifiedName(RenderingContext.createRenderingContext()));
   }
}
