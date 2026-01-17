package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.JavaDsl;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExportsDslTest
{
   @Test
   void exports()
   {
      assertEquals("exports some.package;",
                   JavaDsl.exports("some.package")
                          .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void exportsTo()
   {
      assertEquals("""
                   exports some.package to
                   first.module,
                   second.module;""",
                   JavaDsl.exports()
                          .package_("some.package")
                          .to("first.module")
                          .to(JavaDsl.moduleInfo().name("second.module"))
                          .renderDeclaration(RenderingContext.createRenderingContext()));
   }
}
