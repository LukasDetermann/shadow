package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.Dsl;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
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
                      .renderPackageInfo(RenderingContext.createRenderingContext()));
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
                         .renderPackageInfo(RenderingContext.createRenderingContext()));
   }
}
