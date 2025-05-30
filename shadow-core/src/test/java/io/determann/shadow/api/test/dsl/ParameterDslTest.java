package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParameterDslTest
{
   @Test
   void shortFrom()
   {
      //@start region="short-api"
      assertEquals("ParamType two",
                   Dsl.parameter("ParamType", "two").render(DEFAULT));
      //@end
   }

   @Test
   void allApiOptions()
   {
      //@start region="api"
      assertEquals("@MyAnnotation @MyAnnotation2 final ParamType... one",
                   Dsl.parameter()
                      .annotate(Dsl.annotationUsage().type("MyAnnotation"))
                      .annotate(Dsl.annotationUsage().type("MyAnnotation2"))
                      .final_()
                      .type("ParamType")
                      .name("one")
                      .varArgs()
                      .render(DEFAULT));
      //@end
   }

   @Test
   void annotated()
   {
      assertEquals("@MyAnnotation @MyAnnotation2 ParamType one",
                   Dsl.parameter()
                      .annotate(Dsl.annotationUsage().type("MyAnnotation"))
                      .annotate(Dsl.annotationUsage().type("MyAnnotation2"))
                      .type("ParamType")
                      .name("one")
                      .render(DEFAULT));
   }

   @Test
   void final_()
   {
      assertEquals("final ParamType one",
                   Dsl.parameter()
                      .final_()
                      .type("ParamType")
                      .name("one")
                      .render(DEFAULT));
   }

   @Test
   void varArgs()
   {
      assertEquals("ParamType... one",
                   Dsl.parameter()
                      .type("ParamType")
                      .name("one")
                      .varArgs()
                      .render(DEFAULT));
   }
}
