package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.annotation_processing.TestFactory;
import io.determann.shadow.api.annotation_processing.dsl.JavaDsl;
import io.determann.shadow.api.annotation_processing.dsl.annotation.AnnotationRenderable;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationUsageDslTest
{
   @Test
   void noName()
   {
      assertEquals("@MyType(1)",
                   JavaDsl.annotationUsage()
                          .type("MyType")
                          .noName()
                          .value(JavaDsl.annotationValue(1))
                          .renderDeclaration(createRenderingContext()));
   }

   @Test
   void noNameAndName()
   {
      //@start region="api"
      assertEquals("@MyType(1, second = 5L)",
                   JavaDsl.annotationUsage()
                          .type("MyType")
                          .noName()
                          .value(JavaDsl.annotationValue(1))
                          .name("second")
                          .value("5L")
                          .renderDeclaration(createRenderingContext()));
      //@end
   }

   @Test
   void cAnnotation()
   {
      assertEquals("@org.example.MyAnnotation",
                   JavaDsl.annotationUsage()
                          .type(TestFactory.create(AnnotationRenderable.class, "renderName", "org.example.MyAnnotation"))
                          .renderDeclaration(createRenderingContext()));
   }
}
