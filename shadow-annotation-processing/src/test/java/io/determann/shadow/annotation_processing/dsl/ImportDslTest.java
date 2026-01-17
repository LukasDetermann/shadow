package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.JavaDsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ImportDslTest
{
   @Test
   void declaredDeclared()
   {
      assertEquals("import static org.example.MyClass.*;",
                   JavaDsl.import_().static_().declared(JavaDsl.class_().package_("org.example").name("MyClass")).renderDeclaration(createRenderingContext()));
   }

   @Test
   void importAlString()
   {
      assertEquals("import static org.examole.MyClass.*;",
                   JavaDsl.import_().static_().declared("org.examole.MyClass").renderDeclaration(createRenderingContext()));
   }

   @Test
   void importMethod()
   {
      assertEquals("import static org.example.MyClass.foo;",
                   JavaDsl.import_().static_().method(JavaDsl.class_().package_("org.example").name("MyClass"),
                                                      JavaDsl.method().result("String").name("foo"))
                          .renderDeclaration(createRenderingContext()));
   }

   @Test
   void importField()
   {
      assertEquals("import static org.example.MyClass.A_CONSTANT;",
                   JavaDsl.import_().static_().field(JavaDsl.class_().package_("org.example").name("MyClass"),
                                                     JavaDsl.field().type("String").name("A_CONSTANT"))
                          .renderDeclaration(createRenderingContext()));
   }
}
