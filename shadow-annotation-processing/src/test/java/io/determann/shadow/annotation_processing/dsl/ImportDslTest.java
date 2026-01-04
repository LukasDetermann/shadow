package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.Dsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ImportDslTest
{
   @Test
   void declaredDeclared()
   {
      assertEquals("import static org.example.MyClass.*;",
                   Dsl.import_().static_().declared(Dsl.class_().package_("org.example").name("MyClass")).renderDeclaration(createRenderingContext()));
   }

   @Test
   void importAlString()
   {
      assertEquals("import static org.examole.MyClass.*;",
                   Dsl.import_().static_().declared("org.examole.MyClass").renderDeclaration(createRenderingContext()));
   }

   @Test
   void importMethod()
   {
      assertEquals("import static org.example.MyClass.foo;",
                   Dsl.import_().static_().method(Dsl.class_().package_("org.example").name("MyClass"),
                                                  Dsl.method().result("String").name("foo"))
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void importField()
   {
      assertEquals("import static org.example.MyClass.A_CONSTANT;",
                   Dsl.import_().static_().field(Dsl.class_().package_("org.example").name("MyClass"),
                                                 Dsl.field().type("String").name("A_CONSTANT"))
                      .renderDeclaration(createRenderingContext()));
   }
}
