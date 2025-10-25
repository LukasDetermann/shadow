package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.record.RecordImportStep;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.dsl.Dsl.innerRecord;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecordDslTest
{

   public static final RecordImportStep RECORD = Dsl.record().package_("org.example");

   @Test
   void javadoc()
   {
      assertEquals("""
                   package org.example;
                   
                   /// some java doc
                   record MyRecord() {
                   }""",
                   RECORD.javadoc("/// some java doc")
                         .name("MyRecord")
                         .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void annotate()
   {
      assertEquals("""
                   package org.example;
                   
                   @MyAnnotation
                   @MyAnnotation
                   record MyRecord() {
                   }""",
                   RECORD.annotate("MyAnnotation")
                         .annotate(Dsl.annotationUsage().type("MyAnnotation"))
                         .name("MyRecord")
                         .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void modifier()
   {
      assertEquals("""
                   package org.example;
                   
                   public dings public protected private final static strictfp record MyRecord() {
                   }""",
                   RECORD.modifier(Modifier.PUBLIC)
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
                   package org.example;
                   
                   record MyRecord() <T, V> {
                   }""",
                   RECORD.name("MyRecord")
                         .generic("T")
                         .generic(Dsl.generic("V"))
                         .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void implements_()
   {
      assertEquals("""
                   package org.example;
                   
                   record MyRecord() implements SomeInterface, Another {
                   }""",
                   RECORD.name("MyRecord")
                         .implements_("SomeInterface")
                         .implements_(Dsl.interface_().package_("org.example").name("Another"))
                         .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void body()
   {
      assertEquals("""
                   package org.example;
                   
                   record MyRecord() {
                      // some content
                   }""",
                   RECORD.name("MyRecord")
                         .body("// some content")
                         .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void field()
   {
      assertEquals("""
                   package org.example;
                   
                   record MyRecord() {
                      String s;
                      private int i;
                   }""",
                   RECORD.name("MyRecord")
                         .field("String s;")
                         .field(Dsl.field(Modifier.PRIVATE, "int", "i"))
                         .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void method()
   {
      assertEquals("""
                   package org.example;
                   
                   record MyRecord() {
                      abstract void foo() {}
                      String myMethod() {}
                   
                   }""",
                   RECORD.name("MyRecord")
                         .method("abstract void foo() {}")
                         .method(Dsl.method().result("String").name("myMethod"))
                         .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void inner()
   {
      assertEquals("""
                   package org.example;
                   
                   record MyRecord() {
                      record Inner() {}
                      record Inner2() {
                      }
                   
                   }""",
                   RECORD.name("MyRecord")
                         .inner("record Inner() {}")
                         .inner(innerRecord().outer("org.example.MyRecord").name("Inner2"))
                         .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void staticInitializer()
   {
      assertEquals("""
                   package org.example;
                   
                   record MyRecord() {
                      static {
                      // something
                      }
                   
                   }""",
                   RECORD.name("MyRecord")
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
                   
                   record MyRecord() {
                      MyRecord() {}
                      MyRecord2() {}
                   
                   }""",
                   RECORD.name("MyRecord")
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
                   import static org.example.MyInterface2;
                   import static some.other.package.*;
                   
                   record MyRecord() {
                   }""",
                   Dsl.record()
                      .package_("org.example")
                      .import_("some.thing")
                      .import_(Dsl.import_("Dings"))
                      .import_(Dsl.importAll(Dsl.packageInfo().name("other.package")))
                      .import_(Dsl.staticImport("foo.package"))
                      .import_(Dsl.staticImport(Dsl.annotation().package_("org.example").name("MyInterface2")))
                      .import_(Dsl.staticImportAll(Dsl.packageInfo().name("some.other.package")))
                      .name("MyRecord")
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void component()
   {
      assertEquals("""
                   package org.example;
                   
                   record MyRecord(String s, int i) {
                   }""",
                   RECORD.name("MyRecord")
                         .component(Dsl.recordComponent().type("String").name("s"))
                         .component("int i")
                         .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void renderName()
   {
      assertEquals("MyRecord", RECORD.name("MyRecord").renderName(RenderingContext.createRenderingContext()));
   }

   @Test
   void renderQualifiedName()
   {
      assertEquals("org.example.MyRecord", Dsl.record().package_("org.example").name("MyRecord").renderQualifiedName(RenderingContext.createRenderingContext()));
   }

   @Test
   void renderType()
   {
      assertEquals("MyRecord", RECORD.name("MyRecord").renderType(RenderingContext.createRenderingContext()));
      assertEquals("MyRecord<T, S>", RECORD.name("MyRecord").generic("T").generic("S").renderType(RenderingContext.createRenderingContext()));
   }
}
