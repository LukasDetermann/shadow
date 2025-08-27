package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AutomaticImportTest
{
   @Test
   void simpleImport()
   {
      Assertions.assertEquals("""
                              package org.example;
                              
                              import other.package_.Foo;
                              
                              @Foo
                              class MyClass {
                              }""",
                              Dsl.class_().package_("org.example")
                                 .annotate(Dsl.annotationUsage().type(Dsl.annotation().package_("other.package_").name("Foo")))
                                 .name("MyClass")
                                 .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void nameCollision()
   {
      Assertions.assertEquals("""
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
                                 .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void reuse()
   {
      Assertions.assertEquals("""
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
                                 .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void javaLang()
   {
      Assertions.assertEquals("""
                              package org.example;
                              
                              class MyClass {
                                 String foo() {}
                              
                              }""",
                              Dsl.class_().package_("org.example")
                                 .name("MyClass")
                                 .method(Dsl.method().resultType(Dsl.class_().package_("java.lang").name("String"))
                                            .name("foo"))
                                 .renderDeclaration(RenderingContext.DEFAULT));
   }
}
