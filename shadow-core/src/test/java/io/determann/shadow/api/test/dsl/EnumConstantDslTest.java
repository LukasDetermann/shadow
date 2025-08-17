package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumConstantDslTest
{
   @Test
   void annotate()
   {
      assertEquals("""
                   @MyAnnotation
                   @MyAnnotation1
                   FOO""",
                   Dsl.enumConstant()
                      .annotate("MyAnnotation")
                      .annotate(Dsl.annotationUsage().type("MyAnnotation1"))
                      .name("FOO")
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void javadoc()
   {
      assertEquals("""
                   // some doc
                   FOO""",
                   Dsl.enumConstant()
                      .javadoc("// some doc")
                      .name("FOO")
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void parameter()
   {
      assertEquals("FOO(\"test\", 4)",
                   Dsl.enumConstant()
                      .name("FOO")
                      .parameter("\"test\"")
                      .parameter(Dsl.parameter("int", "4"))
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void body()
   {
      assertEquals("""
                   INSTANCE {
                   @Override
                   public String toString() {
                      return "nope";
                   }
                   }""",
                   Dsl.enumConstant()
                      .name("INSTANCE")
                      .body("""
                            @Override
                            public String toString() {
                               return "nope";
                            }""")
                      .renderDeclaration(DEFAULT));
   }
}
