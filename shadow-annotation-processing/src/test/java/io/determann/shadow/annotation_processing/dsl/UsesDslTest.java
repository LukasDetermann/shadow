package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.Dsl;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
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
