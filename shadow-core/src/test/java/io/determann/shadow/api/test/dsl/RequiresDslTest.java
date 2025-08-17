package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RequiresDslTest
{
   @Test
   void requires()
   {
      assertEquals("requires some.module;",
                   Dsl.requires()
                      .dependency("some.module")
                      .renderDeclaration(DEFAULT));

      assertEquals("requires some.module;",
                   Dsl.requires()
                      .dependency(Dsl.moduleInfo().name("some.module"))
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void requiresTransitive()
   {
      assertEquals("requires transitive some.module;",
                   Dsl.requires()
                      .transitive()
                      .dependency("some.module")
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void requiresStatic()
   {
      assertEquals("requires static some.module;",
                   Dsl.requires()
                      .static_()
                      .dependency("some.module")
                      .renderDeclaration(DEFAULT));
   }
}
