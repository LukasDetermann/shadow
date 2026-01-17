package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.Modifier;
import io.determann.shadow.api.annotation_processing.dsl.JavaDsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldDslTest
{
   @Test
   void javaDoc()
   {
      assertEquals("""
                   /// some javadoc
                   MyType field1;""",
                   JavaDsl.field()
                          .javadoc("/// some javadoc")
                          .type("MyType")
                          .name("field1")
                          .renderDeclaration(createRenderingContext()));
   }

   @Test
   void annotate()
   {
      assertEquals("""
                   @MyAnnotation
                   @AnotherAnnotation
                   MyType someName;""",
                   JavaDsl.field()
                          .annotate("MyAnnotation")
                          .annotate(JavaDsl.annotationUsage()
                                           .type("AnotherAnnotation"))
                          .type("MyType")
                          .name("someName")
                          .renderDeclaration(createRenderingContext()));
   }

   @Test
   void modifiers()
   {
      assertEquals("myModifier abstract public protected private final static strictfp transient volatile MyType modified;",
                   JavaDsl.field()
                          .modifier("myModifier")
                          .modifier(Modifier.ABSTRACT)
                          .public_()
                          .protected_()
                          .private_()
                          .final_()
                          .static_()
                          .strictfp_()
                          .transient_()
                          .volatile_()
                          .type("MyType")
                          .name("modified")
                          .renderDeclaration(createRenderingContext()));
   }

   @Test
   void initializers()
   {
      assertEquals("int i1 = 1, i2 = 2;",
                   JavaDsl.field()
                          .type("int")
                          .name("i1")
                          .initializer("1")
                          .name("i2")
                          .initializer("2")
                          .renderDeclaration(createRenderingContext()));
   }

   @Test
   void api()
   {
      //@start region="api"
      assertEquals("""
                   @MyAnnotation
                   private final static int I1 = 5;""",
                   JavaDsl.field()
                          .annotate("MyAnnotation")
                          .private_()
                          .final_()
                          .static_()
                          .type("int")
                          .name("I1")
                          .initializer("5")
                          .renderDeclaration(createRenderingContext()));
      //@end
   }

   @Test
   void renderNameMultiDeclaration()
   {
      assertEquals("s", JavaDsl.field().type("String").name("s").initializer("\"\"").name("s1").renderName(createRenderingContext()));
   }

   @Test
   void multiDeclaration()
   {
      assertEquals("int i = 0, i1 = 1, i2;",
                   JavaDsl.field()
                          .type("int")
                          .name("i")
                          .initializer("0")
                          .name("i1")
                          .initializer("1")
                          .name("i2")
                          .renderDeclaration(createRenderingContext()));
   }
}
