package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RecordDslTest
{
   @Test
   void javadoc()
   {
      assertEquals("""
                   /// some java doc
                   record MyRecord() {
                   }""",
                   Dsl.innerRecord().javadoc("/// some java doc")
                      .name("MyRecord")
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void annotate()
   {
      assertEquals("""
                   @MyAnnotation
                   @MyAnnotation
                   record MyRecord() {
                   }""",
                   Dsl.innerRecord()
                      .annotate("MyAnnotation")
                      .annotate(Dsl.annotationUsage().type("MyAnnotation"))
                      .name("MyRecord")
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void modifier()
   {
      assertEquals("""
                   public dings public protected private final static strictfp record MyRecord() {
                   }""",
                   Dsl.innerRecord()
                      .modifier(Modifier.PUBLIC)
                      .modifier("dings")
                      .public_()
                      .protected_()
                      .private_()
                      .final_()
                      .static_()
                      .strictfp_()
                      .name("MyRecord")
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void generic()
   {
      assertEquals("""
                   record MyRecord() <T, V> {
                   }""",
                   Dsl.innerRecord()
                      .name("MyRecord")
                      .generic("T")
                      .generic(Dsl.generic("V"))
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void implements_()
   {
      assertEquals("""
                   record MyRecord() implements SomeInterface, Another {
                   }""",
                   Dsl.innerRecord()
                      .name("MyRecord")
                      .implements_("SomeInterface")
                      .implements_(Dsl.innerInterface().name("Another"))
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void body()
   {
      assertEquals("""
                   record MyRecord() {
                      // some content
                   }""",
                   Dsl.innerRecord()
                      .name("MyRecord")
                      .body("// some content")
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void field()
   {
      assertEquals("""
                   record MyRecord() {
                      String s;
                      private int i;
                   }""",
                   Dsl.innerRecord()
                      .name("MyRecord")
                      .field("String s;")
                      .field(Dsl.field(Modifier.PRIVATE, "int", "i"))
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void method()
   {
      assertEquals("""
                   record MyRecord() {
                      abstract void foo() {}
                      String myMethod() {}
                   
                   }""",
                   Dsl.innerRecord()
                      .name("MyRecord")
                      .method("abstract void foo() {}")
                      .method(Dsl.method().result("String").name("myMethod"))
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void inner()
   {
      assertEquals("""
                   record MyRecord() {
                      record Inner() {}
                      record Inner2() {
                      }
                   
                   }""",
                   Dsl.innerRecord()
                      .name("MyRecord")
                      .inner("record Inner() {}")
                      .inner(Dsl.innerRecord().name("Inner2"))
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void staticInitializer()
   {
      assertEquals("""
                   record MyRecord() {
                      static {
                      // something
                      }
                   
                   }""",
                   Dsl.innerRecord()
                      .name("MyRecord")
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
                   record MyRecord() {
                      MyRecord() {}
                      MyRecord2() {}
                   
                   }""",
                   Dsl.innerRecord()
                      .name("MyRecord")
                      .constructor("MyRecord() {}")
                      .constructor(Dsl.constructor().type("MyRecord2"))
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void copyright()
   {
      assertEquals("""
                   // some copyright
                   package org.example;
                   
                   /// some javadoc
                   record MyRecord() {
                   }""",
                   Dsl.record()
                      .copyright("// some copyright")
                      .package_(Dsl.packageInfo().name("org.example"))
                      .javadoc("/// some javadoc")
                      .name("MyRecord")
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void package_()
   {
      assertEquals("""
                   package org.example;
                   
                   record MyRecord() {
                   }""",
                   Dsl.record()
                      .package_("org.example")
                      .name("MyRecord")
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
                   import static MyInterface2;
                   import static some.other.package.*;
                   
                   record MyRecord() {
                   }""",
                   Dsl.record()
                      .package_("org.example")
                      .import_("some.thing")
                      .import_(Dsl.import_("Dings"))
                      .import_(Dsl.importAll(Dsl.packageInfo().name("other.package")))
                      .import_(Dsl.staticImport("foo.package"))
                      .import_(Dsl.staticImport(Dsl.innerAnnotation().name("MyInterface2")))
                      .import_(Dsl.staticImportAll(Dsl.packageInfo().name("some.other.package")))
                      .name("MyRecord")
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void component()
   {
      assertEquals("""
                   record MyRecord(String s, int i) {
                   }""",
                   Dsl.innerRecord()
                         .name("MyRecord")
                         .component(Dsl.recordComponent().type("String").name("s"))
                         .component("int i")
                         .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void renderName()
   {
      assertEquals("MyRecord", Dsl.innerRecord().name("MyRecord").renderName(RenderingContext.createRenderingContext()));
   }

   @Test
   void renderQualifiedName()
   {
      assertEquals("org.example.MyRecord", Dsl.record().package_("org.example").name("MyRecord").renderQualifiedName(RenderingContext.createRenderingContext()));
   }

   @Test
   void renderType()
   {
      assertEquals("MyRecord", Dsl.innerRecord().name("MyRecord").renderType(RenderingContext.createRenderingContext()));
      assertEquals("MyRecord<T, S>", Dsl.innerRecord().name("MyRecord").generic("T").generic("S").renderType(RenderingContext.createRenderingContext()));
   }
}
