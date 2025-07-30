package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Enum;
import io.determann.shadow.api.test.TestFactory;
import io.determann.shadow.api.test.TestProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstructorDslTest
{
   @AfterEach
   void reset()
   {
      TestProvider.reset();
   }

   @Test
   void javadoc()
   {
      assertEquals("""
                   /// some javadoc
                   MyType() {}""",
                   Dsl.constructor().javadoc("/// some javadoc").type("MyType").renderDeclaration(DEFAULT));
   }

   @Test
   void annotate()
   {
      assertEquals("""
                   @MyAnnotation
                   MyType() {}""",
                   Dsl.constructor()
                      .annotate("MyAnnotation")
                      .type("MyType")
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void modifier()
   {
      assertEquals("myModifier private public protected private MyType() {}",
                   Dsl.constructor().modifier("myModifier")
                      .modifier(C_Modifier.PRIVATE)
                      .public_()
                      .protected_()
                      .private_()
                      .type("MyType")
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void generic()
   {
      assertEquals("<T, S> MyType() {}",
                   Dsl.constructor()
                      .generic("T")
                      .generic("S")
                      .type("MyType")
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void type()
   {
      C_Enum cEnum = TestFactory.create(C_Enum.class, "renderQualifiedName", "MyEnum");

      assertEquals("MyEnum() {}", Dsl.constructor().type(cEnum).renderDeclaration(DEFAULT));
   }

   @Test
   void parameter()
   {
      assertEquals("MyType(int i1, int i2, int i3) {}",
                   Dsl.constructor().type("MyType")
                      .parameter("int i1", "int i2")
                      .parameter("int i3")
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void parameterType()
   {
      assertEquals("MyType(int i1, int i2, int i3) {}",
                   Dsl.constructor().type("MyType")
                      .parameter(Dsl.parameter("int", "i1"), Dsl.parameter("int", "i2"))
                      .parameter(Dsl.parameter("int", "i3"))
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void throws_()
   {
      C_Class cClass = TestFactory.create(C_Class.class, "renderName", "MyException3");

      assertEquals("MyType() throws MyException1, MyException2, MyException3 {}",
                   Dsl.constructor()
                      .type("MyType")
                      .throws_("MyException1", "MyException2")
                      .throws_(cClass)
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void body()
   {
      assertEquals("""
                   MyType() {
                   //some content
                   }""",
                   Dsl.constructor()
                      .type("MyType")
                      .body("//some content")
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void api()
   {
      //@start region="api"
      assertEquals("""
                   @MyAnnotation
                   private <T> MyType(int i1, int i2) throws AnException {
                   // some content
                   }""",
                   Dsl.constructor()
                      .annotate("MyAnnotation")
                      .private_()
                      .generic("T")
                      .type("MyType")
                      .parameter(Dsl.parameter("int", "i1"), Dsl.parameter("int", "i2"))
                      .throws_("AnException")
                      .body("// some content")
                      .renderDeclaration(DEFAULT));
      //@end
   }
}
