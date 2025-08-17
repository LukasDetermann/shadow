package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.dsl.RenderingContext.DEFAULT;
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
                      .type(Dsl.innerClass().name("String2"))
                      .name("s")
                      .renderDeclaration(DEFAULT));
   }
}
