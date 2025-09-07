package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.annotation.AnnotationRenderable;
import io.determann.shadow.api.test.TestFactory;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationUsageDslTest
{
   @Test
   void noName()
   {
      assertEquals("@MyType(1)",
                              Dsl.annotationUsage()
                                 .type("MyType")
                                 .noName()
                                 .value(Dsl.annotationValue(1))
                                 .renderDeclaration(createRenderingContext()));
   }

   @Test
   void noNameAndName()
   {
      //@start region="api"
      assertEquals("@MyType(1, second = 5L)",
                   Dsl.annotationUsage()
                      .type("MyType")
                      .noName()
                      .value(Dsl.annotationValue(1))
                      .name("second")
                      .value("5L")
                      .renderDeclaration(createRenderingContext()));
      //@end
   }

   @Test
   void cAnnotation()
   {
      assertEquals("@org.example.MyAnnotation",
                   Dsl.annotationUsage()
                      .type(TestFactory.create(AnnotationRenderable.class, "renderName", "org.example.MyAnnotation"))
                      .renderDeclaration(createRenderingContext()));
   }
}
