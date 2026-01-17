package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.JavaDsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RequiresDslTest
{
   @Test
   void requires()
   {
      assertEquals("requires some.module;",
                   JavaDsl.requires()
                          .dependency("some.module")
                          .renderDeclaration(createRenderingContext()));

      assertEquals("requires some.module;",
                   JavaDsl.requires()
                          .dependency(JavaDsl.moduleInfo().name("some.module"))
                          .renderDeclaration(createRenderingContext()));
   }

   @Test
   void requiresTransitive()
   {
      assertEquals("requires transitive some.module;",
                   JavaDsl.requires()
                          .transitive()
                          .dependency("some.module")
                          .renderDeclaration(createRenderingContext()));
   }

   @Test
   void requiresStatic()
   {
      assertEquals("requires static some.module;",
                   JavaDsl.requires()
                          .static_()
                          .dependency("some.module")
                          .renderDeclaration(createRenderingContext()));
   }
}
