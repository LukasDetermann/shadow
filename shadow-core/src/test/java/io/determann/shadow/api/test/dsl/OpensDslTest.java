package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OpensDslTest
{
   @Test
   void opens()
   {
      Assertions.assertEquals("opens some.package;",
                              Dsl.opens()
                                 .package_("some.package")
                                 .renderDeclaration(RenderingContext.DEFAULT));
   }

   @Test
   void opensTo()
   {
      Assertions.assertEquals("""
                              opens some.package to
                              some.module,
                              another.module;""",
                              Dsl.opens()
                                 .package_("some.package")
                                 .to("some.module")
                                 .to(Dsl.moduleInfo().name("another.module"))
                                 .renderDeclaration(RenderingContext.DEFAULT));
   }
}
