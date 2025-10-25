package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultDslTest
{
   @Test
   void raw()
   {
      assertEquals("MyType", Dsl.result().type("MyType").renderDeclaration(createRenderingContext()));
   }

   @Test
   void annotated()
   {
      //@start region="api"
      assertEquals("@Annotation1 @Annotation2 MyType", Dsl.result()
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
                   Dsl.result()
                      .annotate(Dsl.annotationUsage().type("MyAnnotation"))
                      .type(Dsl.class_().package_("org.example").name("MyType"))
                      .renderDeclaration(createRenderingContext()));
   }
}
