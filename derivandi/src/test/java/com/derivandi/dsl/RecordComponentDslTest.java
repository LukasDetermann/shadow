package com.derivandi.dsl;

import com.derivandi.api.dsl.JavaDsl;
import org.junit.jupiter.api.Test;

import static com.derivandi.api.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecordComponentDslTest
{
   @Test
   void annotate()
   {
      assertEquals("@MyAnnotation @MyAnnotation1 String2 s",
                   JavaDsl.recordComponent()
                          .annotate("MyAnnotation")
                          .annotate(JavaDsl.annotationUsage().type("MyAnnotation1"))
                          .type(JavaDsl.class_().package_("org.example").name("String2"))
                          .name("s")
                          .renderDeclaration(createRenderingContext()));
   }
}
