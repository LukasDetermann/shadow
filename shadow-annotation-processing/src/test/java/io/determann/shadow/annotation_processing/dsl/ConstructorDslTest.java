package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.annotation_processing.TestFactory;
import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.Modifier;
import io.determann.shadow.api.annotation_processing.dsl.JavaDsl;
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
                   JavaDsl.constructor().javadoc("/// some javadoc").type("MyType").renderDeclaration(createRenderingContext()));
   }

   @Test
   void annotate()
   {
      assertEquals("""
                   @MyAnnotation
                   @MyAnnotation2
                   MyType() {}""",
                   JavaDsl.constructor()
                          .annotate("MyAnnotation")
                          .annotate(JavaDsl.annotationUsage().type("MyAnnotation2"))
                          .type("MyType")
                          .renderDeclaration(createRenderingContext()));
   }

   @Test
   void modifier()
   {
      assertEquals("myModifier private public protected private MyType() {}",
                   JavaDsl.constructor().modifier("myModifier")
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
                   JavaDsl.constructor()
                          .genericDeclaration("T")
                          .genericDeclaration("S")
                          .genericDeclaration(JavaDsl.generic("Z"))
                          .type("MyType")
                          .renderDeclaration(createRenderingContext()));
   }

   @Test
   void type()
   {
      Ap.Enum cEnum = TestFactory.create(Ap.Enum.class, "renderSimpleName", "MyEnum");
      Ap.Class cClass = TestFactory.create(Ap.Class.class, "renderSimpleName", "MyClass");
      Ap.Record cRecord = TestFactory.create(Ap.Record.class, "renderSimpleName", "MyRecord");

      assertEquals("MyEnum2() {}", JavaDsl.constructor().type("MyEnum2").renderDeclaration(createRenderingContext()));
      assertEquals("MyEnum() {}", JavaDsl.constructor().type(cEnum).renderDeclaration(createRenderingContext()));
      assertEquals("MyClass() {}", JavaDsl.constructor().type(cClass).renderDeclaration(createRenderingContext()));
      assertEquals("MyRecord() {}", JavaDsl.constructor().type(cRecord).renderDeclaration(createRenderingContext()));
   }

   @Test
   void parameter()
   {
      assertEquals("MyType(int i1, int i2, int i3) {}",
                   JavaDsl.constructor().type("MyType")
                          .parameter("int i1", "int i2")
                          .parameter("int i3")
                          .renderDeclaration(createRenderingContext()));
   }

   @Test
   void parameterType()
   {
      assertEquals("MyType(int i1, int i2, int i3) {}",
                   JavaDsl.constructor().type("MyType")
                          .parameter(JavaDsl.parameter("int", "i1"), JavaDsl.parameter("int", "i2"))
                          .parameter(JavaDsl.parameter("int", "i3"))
                          .renderDeclaration(createRenderingContext()));
   }

   @Test
   void throws_()
   {
      Ap.Class cClass = TestFactory.create(Ap.Class.class, "renderName", "MyException3");

      assertEquals("MyType() throws MyException1, MyException2, MyException3 {}",
                   JavaDsl.constructor()
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
                   JavaDsl.constructor()
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
                   JavaDsl.constructor()
                          .annotate("MyAnnotation")
                          .private_()
                          .genericDeclaration("T")
                          .type("MyType")
                          .parameter(JavaDsl.parameter("int", "i1"), JavaDsl.parameter("int", "i2"))
                          .throws_("AnException")
                          .body("// some content")
                          .renderDeclaration(createRenderingContext()));
      //@end
   }

   @Test
   void receiverAndParam()
   {
      assertEquals("MyType(Other Other.this, String s) {}",
                   JavaDsl.constructor()
                          .type("MyType")
                          .receiver("Other Other.this")
                          .parameter(JavaDsl.parameter("String", "s"))
                          .renderDeclaration(createRenderingContext()));
   }
}
