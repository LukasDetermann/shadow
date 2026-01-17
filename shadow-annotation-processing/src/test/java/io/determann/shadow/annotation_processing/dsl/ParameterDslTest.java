package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.JavaDsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParameterDslTest
{
   @Test
   void shortFrom()
   {
      //@start region="short-api"
      assertEquals("ParamType two",
                   JavaDsl.parameter("ParamType", "two").renderDeclaration(createRenderingContext()));
      //@end
   }

   @Test
   void allApiOptions()
   {
      //@start region="api"
      assertEquals("@MyAnnotation @MyAnnotation2 final ParamType... one",
                   JavaDsl.parameter()
                          .annotate(JavaDsl.annotationUsage().type("MyAnnotation"))
                          .annotate(JavaDsl.annotationUsage().type("MyAnnotation2"))
                          .final_()
                          .type("ParamType")
                          .name("one")
                          .varArgs()
                          .renderDeclaration(createRenderingContext()));
      //@end
   }

   @Test
   void annotated()
   {
      assertEquals("@MyAnnotation @MyAnnotation2 @MyAnnotation3 ParamType one",
                   JavaDsl.parameter()
                          .annotate("MyAnnotation")
                          .annotate(JavaDsl.annotationUsage().type("MyAnnotation2"))
                          .annotate(renderingContext -> "@MyAnnotation3")
                          .type("ParamType")
                          .name("one")
                          .renderDeclaration(createRenderingContext()));
   }

   @Test
   void final_()
   {
      assertEquals("final ParamType one",
                   JavaDsl.parameter()
                          .final_()
                          .type("ParamType")
                          .name("one")
                          .renderDeclaration(createRenderingContext()));
   }

   @Test
   void varArgs()
   {
      assertEquals("ParamType... one",
                   JavaDsl.parameter()
                          .type("ParamType")
                          .name("one")
                          .varArgs()
                          .renderDeclaration(createRenderingContext()));
   }
}
