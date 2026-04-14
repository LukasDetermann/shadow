package com.derivandi.dsl;

import com.derivandi.api.dsl.JavaDsl;
import com.derivandi.api.dsl.RenderingContext;
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
