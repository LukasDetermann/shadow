package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.renderer.RenderingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExportsDslTest
{
   @Test
   void exports()
   {
      assertEquals("exports some.package;",
                   Dsl.exports("some.package")
                         .renderDeclaration(RenderingContext.DEFAULT));
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
                         .renderDeclaration(RenderingContext.DEFAULT));
   }
}
