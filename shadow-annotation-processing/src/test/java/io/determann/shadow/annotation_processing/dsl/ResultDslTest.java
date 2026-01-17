package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.JavaDsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultDslTest
{
   @Test
   void raw()
   {
      assertEquals("MyType", JavaDsl.result().type("MyType").renderDeclaration(createRenderingContext()));
   }

   @Test
   void annotated()
   {
      //@start region="api"
      assertEquals("@Annotation1 @Annotation2 MyType", JavaDsl.result()
                                                              .annotate("Annotation1")
                                                              .annotate("Annotation2")
                                                              .type("MyType")
                                                              .renderDeclaration(createRenderingContext()));
      //@end
   }

   @Test
   void wrapped()
   {
      assertEquals("@MyAnnotation MyType",
                   JavaDsl.result()
                          .annotate(JavaDsl.annotationUsage().type("MyAnnotation"))
                          .type(JavaDsl.class_().package_("org.example").name("MyType"))
                          .renderDeclaration(createRenderingContext()));
   }
}
