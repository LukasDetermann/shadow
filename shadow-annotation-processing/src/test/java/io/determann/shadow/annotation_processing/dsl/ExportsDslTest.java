package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.Dsl;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExportsDslTest
{
   @Test
   void exports()
   {
      assertEquals("exports some.package;",
                   Dsl.exports("some.package")
                         .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void exportsTo()
   {
      assertEquals("""
                   exports some.package to
                   first.module,
                   second.module;""",
                   Dsl.exports()
                         .package_("some.package")
                         .to("first.module")
                         .to(Dsl.moduleInfo().name("second.module"))
                         .renderDeclaration(RenderingContext.createRenderingContext()));
   }
}
