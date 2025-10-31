package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ImportDslTest
{
   @Test
   void importAllDeclared()
   {
      assertEquals("import static org.example.MyClass.*;",
                   Dsl.import_().static_().importAll(Dsl.class_().package_("org.example").name("MyClass")).renderDeclaration(createRenderingContext()));
   }

   @Test
   void importAlString()
   {
      assertEquals("import static org.examole.*;",
                   Dsl.import_().static_().importAll("org.examole").renderDeclaration(createRenderingContext()));
   }

   @Test
   void importMethod()
   {
      assertEquals("import static org.example.MyClass.foo;",
                   Dsl.import_().static_().import_(Dsl.class_().package_("org.example").name("MyClass"),
                                    Dsl.method().result("String").name("foo"))
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void importField()
   {
      assertEquals("import static org.example.MyClass.A_CONSTANT;",
                   Dsl.import_().static_().import_(Dsl.class_().package_("org.example").name("MyClass"),
                                    Dsl.field().type("String").name("A_CONSTANT"))
                      .renderDeclaration(createRenderingContext()));
   }
}
