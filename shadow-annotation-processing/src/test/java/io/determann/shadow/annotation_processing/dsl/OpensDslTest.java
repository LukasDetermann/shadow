package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.JavaDsl;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OpensDslTest
{
   @Test
   void opens()
   {
      Assertions.assertEquals("opens some.package;",
                              JavaDsl.opens()
                                     .package_("some.package")
                                     .renderDeclaration(RenderingContext.createRenderingContext()));
   }

   @Test
   void opensTo()
   {
      Assertions.assertEquals("""
                              opens some.package to
                              some.module,
                              another.module;""",
                              JavaDsl.opens()
                                     .package_("some.package")
                                     .to("some.module")
                                     .to(JavaDsl.moduleInfo().name("another.module"))
                                     .renderDeclaration(RenderingContext.createRenderingContext()));
   }
}
