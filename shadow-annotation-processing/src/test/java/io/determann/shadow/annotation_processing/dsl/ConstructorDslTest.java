package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.annotation_processing.TestFactory;
import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.Modifier;
import io.determann.shadow.api.annotation_processing.dsl.Dsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstructorDslTest
{
   @Test
   void javadoc()
   {
      assertEquals("""
                   /// some javadoc
                   MyType() {}""",
                   Dsl.constructor().javadoc("/// some javadoc").type("MyType").renderDeclaration(createRenderingContext()));
   }

   @Test
   void annotate()
   {
      assertEquals("""
                   @MyAnnotation
                   @MyAnnotation2
                   MyType() {}""",
                   Dsl.constructor()
                      .annotate("MyAnnotation")
                      .annotate(Dsl.annotationUsage().type("MyAnnotation2"))
                      .type("MyType")
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void modifier()
   {
      assertEquals("myModifier private public protected private MyType() {}",
                   Dsl.constructor().modifier("myModifier")
                      .modifier(Modifier.PRIVATE)
                      .public_()
                      .protected_()
                      .private_()
                      .type("MyType")
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void generic()
   {
      assertEquals("<T, S, Z> MyType() {}",
                   Dsl.constructor()
                      .genericDeclaration("T")
                      .genericDeclaration("S")
                      .genericDeclaration(Dsl.generic("Z"))
                      .type("MyType")
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void type()
   {
      Ap.Enum cEnum = TestFactory.create(Ap.Enum.class, "renderSimpleName", "MyEnum");
      Ap.Class cClass = TestFactory.create(Ap.Class.class, "renderSimpleName", "MyClass");
      Ap.Record cRecord = TestFactory.create(Ap.Record.class, "renderSimpleName", "MyRecord");

      assertEquals("MyEnum2() {}", Dsl.constructor().type("MyEnum2").renderDeclaration(createRenderingContext()));
      assertEquals("MyEnum() {}", Dsl.constructor().type(cEnum).renderDeclaration(createRenderingContext()));
      assertEquals("MyClass() {}", Dsl.constructor().type(cClass).renderDeclaration(createRenderingContext()));
      assertEquals("MyRecord() {}", Dsl.constructor().type(cRecord).renderDeclaration(createRenderingContext()));
   }

   @Test
   void parameter()
   {
      assertEquals("MyType(int i1, int i2, int i3) {}",
                   Dsl.constructor().type("MyType")
                      .parameter("int i1", "int i2")
                      .parameter("int i3")
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void parameterType()
   {
      assertEquals("MyType(int i1, int i2, int i3) {}",
                   Dsl.constructor().type("MyType")
                      .parameter(Dsl.parameter("int", "i1"), Dsl.parameter("int", "i2"))
                      .parameter(Dsl.parameter("int", "i3"))
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void throws_()
   {
      Ap.Class cClass = TestFactory.create(Ap.Class.class, "renderName", "MyException3");

      assertEquals("MyType() throws MyException1, MyException2, MyException3 {}",
                   Dsl.constructor()
                      .type("MyType")
                      .throws_("MyException1", "MyException2")
                      .throws_(cClass)
                      .renderDeclaration(createRenderingContext()));
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
                      .renderDeclaration(createRenderingContext()));
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
                      .genericDeclaration("T")
                      .type("MyType")
                      .parameter(Dsl.parameter("int", "i1"), Dsl.parameter("int", "i2"))
                      .throws_("AnException")
                      .body("// some content")
                      .renderDeclaration(createRenderingContext()));
      //@end
   }

   @Test
   void receiverAndParam()
   {
      assertEquals("MyType(Other Other.this, String s) {}",
                   Dsl.constructor()
                      .type("MyType")
                      .receiver("Other Other.this")
                      .parameter(Dsl.parameter("String", "s"))
                      .renderDeclaration(createRenderingContext()));
   }
}
