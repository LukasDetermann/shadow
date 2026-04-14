package com.derivandi.dsl;

import com.derivandi.api.dsl.JavaDsl;
import com.derivandi.api.dsl.RenderingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DslTest
{
   @Test
   void generated()
   {
      String result = JavaDsl.generated("org.example.MyGenerator", "some comment").renderDeclaration(RenderingContext.createRenderingContext());

      assertTrue(result.startsWith("@Generated(value = \"org.example.MyGenerator\", date = \""));
      assertTrue(result.endsWith("\", comments = \"some comment\")"));
   }
}