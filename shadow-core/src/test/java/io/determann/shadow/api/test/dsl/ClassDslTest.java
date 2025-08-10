package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.renderer.RenderingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassDslTest
{
   @Test
   void javadoc()
   {
      assertEquals("""
                   /// some java doc
                   class MyClass {
                   }""",
                   Dsl.innerClass().javadoc("/// some java doc")
                      .name("MyClass")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void annotate()
   {
      assertEquals("""
                   @MyAnnotation
                   @MyAnnotation
                   class MyClass {
                   }""",
                   Dsl.innerClass()
                      .annotate("MyAnnotation")
                      .annotate(Dsl.annotationUsage().type("MyAnnotation"))
                      .name("MyClass")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void modifier()
   {
      assertEquals("""
                   public dings abstract public protected private final sealed non-sealed static strictfp class MyClass {
                   }""",
                   Dsl.innerClass()
                      .modifier(Modifier.PUBLIC)
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
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void generic()
   {
      assertEquals("""
                   class MyClass <T, V> {
                   }""",
                   Dsl.innerClass()
                      .name("MyClass")
                      .generic("T")
                      .generic(Dsl.generic("V"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void extends_()
   {
      assertEquals("""
                   class MyClass extends SomeOther {
                   }""",
                   Dsl.innerClass()
                      .name("MyClass")
                      .extends_("SomeOther")
                      .renderDeclaration(RenderingContext.DEFAULT));

      assertEquals("""
                   class MyClass extends Parent {
                   }""",
                   Dsl.innerClass()
                      .name("MyClass")
                      .extends_(Dsl.innerClass().name("Parent"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void implements_()
   {
      assertEquals("""
                   class MyClass implements SomeInterface, Another {
                   }""",
                   Dsl.innerClass()
                      .name("MyClass")
                      .implements_("SomeInterface")
                      .implements_(Dsl.innerInterface().name("Another"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void permits()
   {
      assertEquals("""
                   class MyClass permits Some, Thing {
                   }""",
                   Dsl.innerClass()
                      .name("MyClass")
                      .permits("Some")
                      .permits(Dsl.innerClass().name("Thing"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void body()
   {
      assertEquals("""
                   class MyClass {
                   // some content
                   }""",
                   Dsl.innerClass()
                      .name("MyClass")
                      .body("// some content")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void field()
   {
      assertEquals("""
                   class MyClass {
                   String s;
                   private int i;
                   }""",
                   Dsl.innerClass()
                      .name("MyClass")
                      .field("String s;")
                      .field(Dsl.field(Modifier.PRIVATE, "int", "i"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void method()
   {
      assertEquals("""
                   class MyClass {
                   abstract void foo() {}
                   String myMethod() {}
                   
                   }""",
                   Dsl.innerClass()
                      .name("MyClass")
                      .method("abstract void foo() {}")
                      .method(Dsl.method().result("String").name("myMethod"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void inner()
   {
      assertEquals("""
                   class MyClass {
                   class Inner {}
                   class Inner2 {
                   }
                   
                   }""",
                   Dsl.innerClass()
                      .name("MyClass")
                      .inner("class Inner {}")
                      .inner(Dsl.innerClass().name("Inner2"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void instanceInitializer()
   {
      assertEquals("""
                   class MyClass {
                   {
                   // something
                   }
                   
                   }""",
                   Dsl.innerClass()
                      .name("MyClass")
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
                   class MyClass {
                   static {
                   // something
                   }
                   
                   }""",
                   Dsl.innerClass()
                      .name("MyClass")
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
                   class MyClass {
                   MyClass() {}
                   MyClass2() {}
                   
                   }""",
                   Dsl.innerClass()
                      .name("MyClass")
                      .constructor("MyClass() {}")
                      .constructor(Dsl.constructor().type("MyClass2"))
                      .renderDeclaration(RenderingContext.DEFAULT));
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
                      .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void package_()
   {
      assertEquals("""
                   package org.example;
                   
                   class MyClass {
                   }""",
                   Dsl.class_()
                      .package_("org.example")
                      .name("MyClass")
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
                   
                   class MyClass {
                   }""",
                   Dsl.class_()
                      .package_("org.example")
                      .import_("some.thing")
                      .import_(Dsl.import_("Dings"))
                      .import_(Dsl.importAll(Dsl.packageInfo().name("other.package")))
                      .import_(Dsl.staticImport("foo.package"))
                      .import_(Dsl.staticImport(Dsl.innerAnnotation().name("MyInterface2")))
                      .import_(Dsl.staticImportAll(Dsl.packageInfo().name("some.other.package")))
                      .name("MyClass")
                      .renderDeclaration(RenderingContext.DEFAULT));
   }
}
