package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DslTest
{
   @Test
   void generated()
   {
      String result = Dsl.generated("org.example.MyGenerator", "some comment").renderDeclaration(RenderingContext.DEFAULT);

      assertTrue(result.startsWith("@Generated(\"org.example.MyGenerator\", date = \""));
      assertTrue(result.endsWith("\", comments = \"some comment\")"));
   }
}