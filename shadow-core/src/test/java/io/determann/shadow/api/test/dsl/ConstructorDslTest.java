package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.C;
import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.test.TestFactory;
import io.determann.shadow.api.test.TestProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.dsl.RenderingContext.DEFAULT;
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
                   @MyAnnotation2
                   MyType() {}""",
                   Dsl.constructor()
                      .annotate("MyAnnotation")
                      .annotate(Dsl.annotationUsage().type("MyAnnotation2"))
                      .type("MyType")
                      .renderDeclaration(DEFAULT));
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
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void generic()
   {
      assertEquals("<T, S, Z> MyType() {}",
                   Dsl.constructor()
                      .generic("T")
                      .generic("S")
                      .generic(Dsl.generic("Z"))
                      .type("MyType")
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void type()
   {
      C.Enum cEnum = TestFactory.create(C.Enum.class, "renderQualifiedName", "MyEnum");
      C.Class cClass = TestFactory.create(C.Class.class, "renderQualifiedName", "MyClass");
      C.Record cRecord = TestFactory.create(C.Record.class, "renderQualifiedName", "MyRecord");

      assertEquals("MyEnum2() {}", Dsl.constructor().type("MyEnum2").renderDeclaration(DEFAULT));
      assertEquals("MyEnum() {}", Dsl.constructor().type(cEnum).renderDeclaration(DEFAULT));
      assertEquals("MyClass() {}", Dsl.constructor().type(cClass).renderDeclaration(DEFAULT));
      assertEquals("MyRecord() {}", Dsl.constructor().type(cRecord).renderDeclaration(DEFAULT));
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
      C.Class cClass = TestFactory.create(C.Class.class, "renderName", "MyException3");

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

   @Test
   void receiverAndParam()
   {
      assertEquals("MyType(Other Other.this, String s) {}",
                   Dsl.constructor()
                      .type("MyType")
                      .receiver("Other Other.this")
                      .parameter(Dsl.parameter("String", "s"))
                      .renderDeclaration(DEFAULT));
   }
}
