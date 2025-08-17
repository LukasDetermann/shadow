package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ImportDslTest
{
   @Test
   void importAllDeclared()
   {
      assertEquals("import static org.example.MyClass.*;",
                   Dsl.staticImportAll(Dsl.class_().package_("org.example").name("MyClass")).renderDeclaration(DEFAULT));
   }

   @Test
   void importAlString()
   {
      assertEquals("import static org.examole.*;",
                   Dsl.staticImportAll("org.examole").renderDeclaration(DEFAULT));
   }

   @Test
   void importMethod()
   {
      assertEquals("import static org.example.MyClass.foo;",
                   Dsl.staticImport(Dsl.class_().package_("org.example").name("MyClass"),
                                    Dsl.method().result("String").name("foo"))
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void importField()
   {
      assertEquals("import static org.example.MyClass.A_CONSTANT;",
                   Dsl.staticImport(Dsl.class_().package_("org.example").name("MyClass"),
                                    Dsl.field().type("String").name("A_CONSTANT"))
                      .renderDeclaration(DEFAULT));
   }
}
