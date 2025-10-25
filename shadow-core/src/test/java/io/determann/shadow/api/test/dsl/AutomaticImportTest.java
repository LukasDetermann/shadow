package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.class_.ClassGenericStep;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.dsl.RenderingConfiguration.DEFAULT_CONFIGURATION;
import static io.determann.shadow.api.dsl.RenderingConfiguration.renderingConfigurationBuilder;
import static io.determann.shadow.api.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AutomaticImportTest
{
   @Test
   void simpleImport()
   {
      assertEquals("""
                   package org.example;
                   
                   import other.package_.Foo;
                   
                   @Foo
                   class MyClass {
                   }""",
                   Dsl.class_().package_("org.example")
                      .annotate(Dsl.annotationUsage().type(Dsl.annotation().package_("other.package_").name("Foo")))
                      .name("MyClass")
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void nameCollision()
   {
      assertEquals("""
                   package org.example;
                   
                   import other.package_.Foo;
                   
                   @Foo
                   @other.package2.Foo
                   class MyClass {
                   }""",
                   Dsl.class_().package_("org.example")
                      .annotate(Dsl.annotationUsage().type(Dsl.annotation().package_("other.package_").name("Foo")))
                      .annotate(Dsl.annotationUsage().type(Dsl.annotation().package_("other.package2").name("Foo")))
                      .name("MyClass")
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void reuse()
   {
      assertEquals("""
                   package org.example;
                   
                   import other.package_.Foo;
                   
                   @Foo
                   @Foo
                   class MyClass {
                   }""",
                   Dsl.class_().package_("org.example")
                      .annotate(Dsl.annotationUsage().type(Dsl.annotation().package_("other.package_").name("Foo")))
                      .annotate(Dsl.annotationUsage().type(Dsl.annotation().package_("other.package_").name("Foo")))
                      .name("MyClass")
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void javaLang()
   {
      assertEquals("""
                   package org.example;
                   
                   class MyClass {
                      String foo() {}
                   
                   }""",
                   Dsl.class_().package_("org.example")
                      .name("MyClass")
                      .method(Dsl.method().resultType(Dsl.class_().package_("java.lang").name("String"))
                                 .name("foo"))
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void disabled()
   {
      assertEquals("""
                   package org.example;
                   
                   @other.package_.Foo
                   @org.example.Foo2
                   class MyClass {
                   }""",
                   Dsl.class_().package_("org.example")
                      .annotate(Dsl.annotationUsage().type(Dsl.annotation().package_("other.package_").name("Foo")))
                      .annotate(Dsl.annotationUsage().type(Dsl.annotation().package_("org.example").name("Foo2")))
                      .name("MyClass")
                      .renderDeclaration(createRenderingContext(renderingConfigurationBuilder(DEFAULT_CONFIGURATION)
                                                                      .withoutAutomaticImports()
                                                                      .build())));
   }

   @Test
   void noPackageKnownType()
   {
      assertEquals("""
                   package org.example;
                   
                   @Foo
                   @Foo
                   class MyClass {
                   }""",
                   Dsl.class_().package_("org.example")
                      .annotate(Dsl.annotationUsage().type(Dsl.annotation().noPackage().name("Foo")))
                      .annotate(Dsl.annotationUsage().type(Dsl.annotation().noPackage().name("Foo")))
                      .name("MyClass")
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void noPackageCollidesWithImported()
   {
      ClassGenericStep step = Dsl.class_().package_("org.example")
                                 .annotate(Dsl.annotationUsage().type(Dsl.annotation().package_("some.other").name("Foo")))
                                 .annotate(Dsl.annotationUsage().type(Dsl.annotation().noPackage().name("Foo")))
                                 .name("MyClass");

      RenderingContext renderingContext = createRenderingContext();

      assertThrows(IllegalStateException.class,
                   () -> step.renderDeclaration(renderingContext),
                   "Cannot import type from unnamed package \"Foo\" it clashes with the already imported type \"some.other.Foo\". Use RenderingContextBuilder#withoutAutomaticImports to disable auto importing.");
   }

   @Test
   void noPackageCollidesWithJavaLang()
   {
      ClassGenericStep step = Dsl.class_().package_("org.example")
                                 .annotate(Dsl.annotationUsage().type(Dsl.annotation().package_("java.lang").name("Foo")))
                                 .annotate(Dsl.annotationUsage().type(Dsl.annotation().noPackage().name("Foo")))
                                 .name("MyClass");

      RenderingContext renderingContext = createRenderingContext();

      assertThrows(IllegalStateException.class,
                   () -> step.renderDeclaration(renderingContext),
                   "Cannot import type from unnamed package \"Foo\" it clashes with the already used type \"java.lang.Foo\". Use RenderingContextBuilder#withoutAutomaticImports to disable auto importing.");
   }

   @Test
   void javaLangWithoutImport()
   {
      assertEquals("""
                   package org.example;
                   
                   @Foo
                   @Foo
                   class MyClass {
                   }""",
                   Dsl.class_().package_("org.example")
                      .annotate(Dsl.annotationUsage().type(Dsl.annotation().package_("java.lang").name("Foo")))
                      .annotate(Dsl.annotationUsage().type(Dsl.annotation().package_("java.lang").name("Foo")))
                      .name("MyClass")
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void javaLangImported()
   {
      assertEquals("""
                   package org.example;
                   
                   import some.other.Foo;
                   
                   @Foo
                   @java.lang.Foo
                   class MyClass {
                   }""",
                   Dsl.class_().package_("org.example")
                      .annotate(Dsl.annotationUsage().type(Dsl.annotation().package_("some.other").name("Foo")))
                      .annotate(Dsl.annotationUsage().type(Dsl.annotation().package_("java.lang").name("Foo")))
                      .name("MyClass")
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void javaLangNoPackage()
   {
      assertEquals("""
                   package org.example;
                   
                   @Foo
                   @java.lang.Foo
                   class MyClass {
                   }""",
                   Dsl.class_().package_("org.example")
                      .annotate(Dsl.annotationUsage().type(Dsl.annotation().noPackage().name("Foo")))
                      .annotate(Dsl.annotationUsage().type(Dsl.annotation().package_("java.lang").name("Foo")))
                      .name("MyClass")
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void innerClass()
   {
      assertEquals("""
                   package org.example;
                   
                   import some.other.Outer;
                   
                   @Outer.Inner
                   @Outer.OtherInner
                   class MyClass {
                   }""",
                   Dsl.class_().package_("org.example")
                      .annotate(Dsl.annotationUsage().type(Dsl.annotation().package_("some.other").name("Outer.Inner")))
                      .annotate(Dsl.annotationUsage().type(Dsl.annotation().package_("some.other").name("Outer.OtherInner")))
                      .name("MyClass")
                      .renderDeclaration(createRenderingContext()));
   }
}