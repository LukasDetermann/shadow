package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.Dsl;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProvidesDslTest
{
   @Test
   void provides()
   {
      assertEquals("provides some.Service with some.Implementation;",
                   Dsl.provides().service(Dsl.class_().package_("some").name("Service"))
                      .with("some.Implementation")
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }
   @Test
   void providesWithMany()
   {
      assertEquals("""
                   provides some.Service with
                   some.Implementation,
                   another.Implementation;""",
                   Dsl.provides().service("some.Service")
                      .with("some.Implementation")
                      .with(Dsl.class_().package_("another").name("Implementation"))
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }
}
