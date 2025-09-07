package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsesDslTest
{
   @Test
   void uses()
   {
      assertEquals("uses some.Service;",
                   Dsl.uses("some.Service")
                      .renderDeclaration(RenderingContext.createRenderingContext()));
   }
}
