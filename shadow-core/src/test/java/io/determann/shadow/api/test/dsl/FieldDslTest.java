package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import io.determann.shadow.api.test.TestProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.api.test.TestProvider.IMPLEMENTATION;
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
                      .render(DEFAULT));
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
                      .render(DEFAULT));
   }

   @Test
   void modifiers()
   {
      assertEquals("myModifier abstract public protected private final static strictfp transient volatile MyType modified;",
                   Dsl.field()
                      .modifier("myModifier")
                      .modifier(C_Modifier.ABSTRACT)
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
                      .render(DEFAULT));
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
                      .render(DEFAULT));
   }

   @Test
   void constantStringStringString()
   {
      //@start region="constantStringStringString"
      assertEquals("private static final int I1 = 5;", Dsl.constant("int", "I1", "5").render(DEFAULT));
      //@end
   }

   @Test
   void constantTypeStringString()
   {
      C_Primitive cPrimitive = () -> IMPLEMENTATION;
      TestProvider.addValue("int");

      //@start region="constantTypeStringString"
      assertEquals("private static final int I1 = 5;", Dsl.constant(cPrimitive, "I1", "5").render(DEFAULT));
      //@end
   }

   @Test
   void constantModifierStringStringString()
   {
      //@start region="constantModifierStringStringString"
      assertEquals("public final static int I1 = 5;", Dsl.constant(C_Modifier.PUBLIC, "int", "I1", "5").render(DEFAULT));
      //@end
   }

   @Test
   void constantModifierTypeStringString()
   {
      C_Primitive cPrimitive = () -> IMPLEMENTATION;
      TestProvider.addValue("int");

      //@start region="constantModifierTypeStringString"
      assertEquals("public final static int I1 = 5;", Dsl.constant(C_Modifier.PUBLIC, cPrimitive, "I1", "5").render(DEFAULT));
      //@end
   }

   @Test
   void fieldStringString()
   {
      //@start region="fieldStringString"
      assertEquals("private int i1;", Dsl.field("int", "i1").render(DEFAULT));
      //@end
   }

   @Test
   void fieldTypeString()
   {
      C_Primitive cPrimitive = () -> IMPLEMENTATION;
      TestProvider.addValue("int");

      //@start region="fieldTypeString"
      assertEquals("private int i1;", Dsl.field(cPrimitive, "i1").render(DEFAULT));
      //@end
   }

   @Test
   void fieldStringStringString()
   {
      //@start region="fieldStringStringString"
      assertEquals("private int i1 = 5;", Dsl.field("int", "i1", "5").render(DEFAULT));
      //@end
   }

   @Test
   void fieldTypeStringString()
   {
      C_Primitive cPrimitive = () -> IMPLEMENTATION;
      TestProvider.addValue("int");

      //@start region="fieldTypeStringString"
      assertEquals("private int i1 = 5;", Dsl.field(cPrimitive, "i1", "5").render(DEFAULT));
      //@end
   }

   @Test
   void fieldModifierStringString()
   {
      //@start region="fieldModifierStringString"
      assertEquals("public int i1;", Dsl.field(C_Modifier.PUBLIC, "int", "i1").render(DEFAULT));
      //@end
   }

   @Test
   void fieldModifierTypeString()
   {
      C_Primitive cPrimitive = () -> IMPLEMENTATION;
      TestProvider.addValue("int");

      //@start region="fieldModifierTypeString"
      assertEquals("public int i1;", Dsl.field(C_Modifier.PUBLIC, cPrimitive, "i1").render(DEFAULT));
      //@end
   }

   @Test
   void fieldModifierStringStringString()
   {
      //@start region="fieldModifierStringStringString"
      assertEquals("public int i1 = 5;", Dsl.field(C_Modifier.PUBLIC, "int", "i1", "5").render(DEFAULT));
      //@end
   }

   @Test
   void fieldModifierTypeStringString()
   {
      C_Primitive cPrimitive = () -> IMPLEMENTATION;
      TestProvider.addValue("int");

      //@start region="fieldModifierTypeStringString"
      assertEquals("public int i1 = 5;", Dsl.field(C_Modifier.PUBLIC, cPrimitive, "i1", "5").render(DEFAULT));
      //@end
   }
}
