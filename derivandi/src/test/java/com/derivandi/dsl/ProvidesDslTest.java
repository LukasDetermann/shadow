package com.derivandi.dsl;

import com.derivandi.api.dsl.JavaDsl;
import com.derivandi.api.dsl.RenderingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProvidesDslTest
{
   @Test
   void provides()
   {
      assertEquals("provides some.Service with some.Implementation;",
                   JavaDsl.provides().service(JavaDsl.class_().package_("some").name("Service"))
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
                   JavaDsl.provides().service("some.Service")
                          .with("some.Implementation")
                          .with(JavaDsl.class_().package_("another").name("Implementation"))
                          .renderDeclaration(RenderingContext.createRenderingContext()));
   }
}
