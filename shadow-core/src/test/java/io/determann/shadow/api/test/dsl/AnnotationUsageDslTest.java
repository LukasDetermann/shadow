package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.shadow.structure.C_Package;
import io.determann.shadow.api.test.TestProvider;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.api.test.TestProvider.IMPLEMENTATION;
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
                                 .render(DEFAULT));
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
                      .render(DEFAULT));
      //@end
   }

   @Test
   void cAnnotation()
   {
      TestProvider.addValue((C_Package) () -> IMPLEMENTATION);
      TestProvider.addValue(false);
      TestProvider.addValue(false);
      TestProvider.addValue("org.example.MyAnnotation");
      assertEquals("@org.example.MyAnnotation", Dsl.annotationUsage().type(() -> IMPLEMENTATION).render(DEFAULT));
   }
}
