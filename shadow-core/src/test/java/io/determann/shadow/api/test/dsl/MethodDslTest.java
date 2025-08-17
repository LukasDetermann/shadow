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

class MethodDslTest
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
                   MyType foo() {}""",
                   Dsl.method().javadoc("/// some javadoc").result("MyType").name("foo").renderDeclaration(DEFAULT));
   }

   @Test
   void annotate()
   {
      assertEquals("""
                   @MyAnnotation
                   @MyAnnotation2
                   MyType foo() {}""",
                   Dsl.method()
                      .annotate("MyAnnotation")
                      .annotate(Dsl.annotationUsage().type("MyAnnotation2"))
                      .result(Dsl.result().type("MyType"))
                      .name("foo")
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void modifier()
   {
      assertEquals("myModifier private abstract public protected private default final native static strictfp MyType foo() {}",
                   Dsl.method().modifier("myModifier")
                      .modifier(Modifier.PRIVATE)
                      .abstract_()
                      .public_()
                      .protected_()
                      .private_()
                      .default_()
                      .final_()
                      .native_()
                      .static_()
                      .strictfp_()
                      .result("MyType")
                      .name("foo")
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void generic()
   {
      assertEquals("<T, S, V> MyType foo() {}",
                   Dsl.method()
                      .generic("T")
                      .generic("S")
                      .generic(Dsl.generic("V"))
                      .result("MyType")
                      .name("foo")
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void type()
   {
      C.Enum cEnum = TestFactory.create(C.Enum.class, "renderName", "MyEnum");

      assertEquals("MyEnum foo() {}", Dsl.method().resultType(cEnum).name("foo").renderDeclaration(DEFAULT));
   }

   @Test
   void parameter()
   {
      assertEquals("MyType foo(int i1, int i2, int i3) {}",
                   Dsl.method().resultType("MyType")
                      .name("foo")
                      .parameter("int i1", "int i2")
                      .parameter("int i3")
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void parameterType()
   {
      assertEquals("MyType foo(int i1, int i2, int i3) {}",
                   Dsl.method().resultType("MyType")
                      .name("foo")
                      .parameter(Dsl.parameter("int", "i1"), Dsl.parameter("int", "i2"))
                      .parameter(Dsl.parameter("int", "i3"))
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void throws_()
   {
      C.Class cClass = TestFactory.create(C.Class.class, "renderName", "MyException3");

      assertEquals("MyType foo() throws MyException1, MyException2, MyException3 {}",
                   Dsl.method()
                      .resultType("MyType")
                      .name("foo")
                      .throws_("MyException1", "MyException2")
                      .throws_(cClass)
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void body()
   {
      assertEquals("""
                   MyType foo() {
                   //some content
                   }""",
                   Dsl.method()
                      .resultType("MyType")
                      .name("foo")
                      .body("//some content")
                      .renderDeclaration(DEFAULT));
   }

   @Test
   void api()
   {
      //@start region="api"
      assertEquals("""
                   @MyAnnotation
                   private <T> MyType foo(int i1, int i2) throws AnException {
                   // some content
                   }""",
                   Dsl.method()
                      .annotate("MyAnnotation")
                      .private_()
                      .generic("T")
                      .resultType("MyType")
                      .name("foo")
                      .parameter(Dsl.parameter("int", "i1"), Dsl.parameter("int", "i2"))
                      .throws_("AnException")
                      .body("// some content")
                      .renderDeclaration(DEFAULT));
      //@end
   }

   @Test
   void renderName()
   {
      assertEquals("foo", Dsl.method().resultType("void").name("foo").renderName(DEFAULT));
   }

   @Test
   void receiver()
   {
      assertEquals("MyType foo(MyClass MyClass.this) {}",
                   Dsl.method().resultType("MyType").name("foo").receiver("MyClass MyClass.this").renderDeclaration(DEFAULT));
   }
}
