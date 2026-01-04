package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.annotation_processing.TestFactory;
import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.Modifier;
import io.determann.shadow.api.annotation_processing.dsl.Dsl;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MethodDslTest
{
   @Test
   void javadoc()
   {
      assertEquals("""
                   /// some javadoc
                   MyType foo() {}""",
                   Dsl.method().javadoc("/// some javadoc").result("MyType").name("foo").renderDeclaration(createRenderingContext()));
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
                      .renderDeclaration(createRenderingContext()));
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
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void generic()
   {
      assertEquals("<T, S, V> MyType foo() {}",
                   Dsl.method()
                      .genericDeclaration("T")
                      .genericDeclaration("S")
                      .genericDeclaration(Dsl.generic("V"))
                      .result("MyType")
                      .name("foo")
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void type()
   {
      Ap.Enum cEnum = TestFactory.create(Ap.Enum.class, "renderType", "MyEnum");

      assertEquals("MyEnum foo() {}", Dsl.method().resultType(cEnum).name("foo").renderDeclaration(createRenderingContext()));
   }

   @Test
   void parameter()
   {
      assertEquals("MyType foo(int i1, int i2, int i3) {}",
                   Dsl.method().resultType("MyType")
                      .name("foo")
                      .parameter("int i1", "int i2")
                      .parameter("int i3")
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void parameterType()
   {
      assertEquals("MyType foo(int i1, int i2, int i3) {}",
                   Dsl.method().resultType("MyType")
                      .name("foo")
                      .parameter(Dsl.parameter("int", "i1"), Dsl.parameter("int", "i2"))
                      .parameter(Dsl.parameter("int", "i3"))
                      .renderDeclaration(createRenderingContext()));
   }

   @Test
   void throws_()
   {
      Ap.Class cClass = TestFactory.create(Ap.Class.class, "renderName", "MyException3");

      assertEquals("MyType foo() throws MyException1, MyException2, MyException3 {}",
                   Dsl.method()
                      .resultType("MyType")
                      .name("foo")
                      .throws_("MyException1", "MyException2")
                      .throws_(cClass)
                      .renderDeclaration(createRenderingContext()));
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
                      .renderDeclaration(createRenderingContext()));
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
                      .genericDeclaration("T")
                      .resultType("MyType")
                      .name("foo")
                      .parameter(Dsl.parameter("int", "i1"), Dsl.parameter("int", "i2"))
                      .throws_("AnException")
                      .body("// some content")
                      .renderDeclaration(createRenderingContext()));
      //@end
   }

   @Test
   void renderName()
   {
      assertEquals("foo", Dsl.method().resultType("void").name("foo").renderName(createRenderingContext()));
   }

   @Test
   void receiver()
   {
      assertEquals("MyType foo(MyClass MyClass.this) {}",
                   Dsl.method().resultType("MyType").name("foo").receiver("MyClass MyClass.this").renderDeclaration(createRenderingContext()));
   }
}
