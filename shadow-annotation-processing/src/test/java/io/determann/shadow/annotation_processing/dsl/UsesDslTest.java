package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.JavaDsl;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsesDslTest
{
   @Test
   void uses()
   {
      assertEquals("uses some.Service;",
                   JavaDsl.uses("some.Service")
                          .renderDeclaration(RenderingContext.createRenderingContext()));
   }
}
