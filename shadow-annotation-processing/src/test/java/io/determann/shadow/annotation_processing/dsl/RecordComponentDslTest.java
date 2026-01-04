package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.Dsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecordComponentDslTest
{
   @Test
   void annotate()
   {
      assertEquals("@MyAnnotation @MyAnnotation1 String2 s",
                   Dsl.recordComponent()
                      .annotate("MyAnnotation")
                      .annotate(Dsl.annotationUsage().type("MyAnnotation1"))
                      .type(Dsl.class_().package_("org.example").name("String2"))
                      .name("s")
                      .renderDeclaration(createRenderingContext()));
   }
}
