package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.test.TestProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldDslTest
{
   @AfterEach
   void reset()
   {
      TestProvider.reset();
   }

   @Test
   void javaDoc()
   {
      assertEquals("""
                   /// some javadoc
                   MyType field1;""",
                   Dsl.field()
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
                   Dsl.field()
                      .annotate("MyAnnotation")
                      .annotate(Dsl.annotationUsage()
                                   .type("AnotherAnnotation"))
                      .type("MyType")
                      .name("someName")
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void modifiers()
   {
      assertEquals("myModifier abstract public protected private final static strictfp transient volatile MyType modified;",
                   Dsl.field()
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
                   Dsl.field()
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
                   Dsl.field()
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
      assertEquals("s", Dsl.field().type("String").name("s").initializer("\"\"").name("s1").renderName(createRenderingContext()));
   }

   @Test
   void multiDeclaration()
   {
      assertEquals("int i = 0, i1 = 1, i2;",
                   Dsl.field()
                      .type("int")
                      .name("i")
                      .initializer("0")
                      .name("i1")
                      .initializer("1")
                      .name("i2")
                      .renderDeclaration(createRenderingContext()));
   }
}
