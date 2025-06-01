package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Package;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Enum;
import io.determann.shadow.api.test.TestProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.api.test.TestProvider.IMPLEMENTATION;
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
                   Dsl.method().javadoc("/// some javadoc").result("MyType").name("foo").render(DEFAULT));
   }

   @Test
   void annotate()
   {
      assertEquals("""
                   @MyAnnotation
                   MyType foo() {}""",
                   Dsl.method()
                      .annotate("MyAnnotation")
                      .result("MyType")
                      .name("foo")
                      .render(DEFAULT));
   }

   @Test
   void modifier()
   {
      assertEquals("myModifier private abstract public protected private default final native static strictfp MyType foo() {}",
                   Dsl.method().modifier("myModifier")
                      .modifier(C_Modifier.PRIVATE)
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
                      .render(DEFAULT));
   }

   @Test
   void generic()
   {
      assertEquals("<T, S> MyType foo() {}",
                   Dsl.method()
                      .generic("T")
                      .generic("S")
                      .result("MyType")
                      .name("foo")
                      .render(DEFAULT));
   }

   @Test
   void type()
   {
      TestProvider.addValue((C_Package) () -> IMPLEMENTATION);
      TestProvider.addValue(false);
      TestProvider.addValue(false);
      TestProvider.addValue("MyEnum");
      C_Enum cEnum = () -> IMPLEMENTATION;

      assertEquals("MyEnum foo() {}", Dsl.method().resultType(cEnum).name("foo").render(DEFAULT));
   }

   @Test
   void parameter()
   {
      assertEquals("MyType foo(int i1, int i2, int i3) {}",
                   Dsl.method().resultType("MyType")
                      .name("foo")
                      .parameter("int i1", "int i2")
                      .parameter("int i3")
                      .render(DEFAULT));
   }

   @Test
   void parameterType()
   {
      assertEquals("MyType foo(int i1, int i2, int i3) {}",
                   Dsl.method().resultType("MyType")
                      .name("foo")
                      .parameter(Dsl.parameter("int", "i1"), Dsl.parameter("int", "i2"))
                      .parameter(Dsl.parameter("int", "i3"))
                      .render(DEFAULT));
   }

   @Test
   void throws_()
   {
      TestProvider.addValue((C_Package) () -> IMPLEMENTATION);
      TestProvider.addValue(false);
      TestProvider.addValue(false);
      TestProvider.addValue("MyException3");
      TestProvider.addValue(Collections.emptyList());
      C_Class cClass = () -> IMPLEMENTATION;

      assertEquals("MyType foo() throws MyException1, MyException2, MyException3 {}",
                   Dsl.method()
                      .resultType("MyType")
                      .name("foo")
                      .throws_("MyException1", "MyException2")
                      .throws_(cClass)
                      .render(DEFAULT));
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
                      .render(DEFAULT));
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
                      .render(DEFAULT));
      //@end
   }
}
