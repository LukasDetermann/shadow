package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProvidesDslTest
{
   @Test
   void provides()
   {
      assertEquals("provides some.Service with some.Implementation;",
                   Dsl.provides().service(Dsl.innerClass().name("some.Service"))
                      .with("some.Implementation")
                      .renderDeclaration(RenderingContext.DEFAULT));
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
                      .with(Dsl.innerClass().name("another.Implementation"))
                      .renderDeclaration(RenderingContext.DEFAULT));
   }
}
