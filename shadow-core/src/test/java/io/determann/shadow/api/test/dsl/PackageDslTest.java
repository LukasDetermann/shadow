package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.renderer.RenderingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PackageDslTest
{
   @Test
   void api()
   {
      assertEquals("""
                   /// someJavadoc
                   @MyAnnotation
                   package org.example;""",
                   Dsl.packageInfo()
                      .javadoc("/// someJavadoc")
                      .annotate("MyAnnotation")
                      .name("org.example")
                      .renderPackageInfo(RenderingContext.DEFAULT));
   }

   @Test
   void annotate()
   {
      assertEquals("""
                   @MyAnnotation
                   @MyAnnotation2
                   package org.example;""",
                   Dsl.packageInfo()
                      .annotate("MyAnnotation")
                      .annotate(Dsl.annotationUsage().type("MyAnnotation2"))
                      .name("org.example")
                         .renderPackageInfo(RenderingContext.DEFAULT));
   }
}
