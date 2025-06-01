package io.determann.shadow.api.test.dsl;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.generic.GenericRenderable;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericDslTest
{
   @Test
   void annotate()
   {
      assertEquals("@MyAnnotation1 @MyAnnotation2 T",
                   Dsl.generic()
                      .annotate("MyAnnotation1")
                      .annotate("MyAnnotation2")
                      .name("T")
                      .render(DEFAULT));
   }

   @Test
   void extends_()
   {
      assertEquals("T extends MyClass & MyInterface",
                   Dsl.generic()
                      .name("T")
                      .extends_("MyClass")
                      .extends_("MyInterface")
                      .render(DEFAULT));
   }

   @Test
   void nested()
   {
      GenericRenderable bound = Dsl.generic()
                                   .name("A")
                                   .extends_("MyClass");

      assertEquals("B extends A",
                   Dsl.generic()
                      .name("B")
                      .extends_(bound)
                      .render(DEFAULT));
   }

   @Test
   void usage()
   {
      GenericRenderable generic = Dsl.generic().name("T").extends_("MyClass");

      assertEquals("T t", Dsl.parameter().type(generic).name("t").render(DEFAULT));
   }

   @Test
   void api()
   {
      //@start region="api"
      assertEquals("T extends MyClass", Dsl.generic().name("T").extends_("MyClass").render(DEFAULT));
      //@end
   }

   @Test
   void recursive()
   {
      //@start region="api"
      GenericRenderable a = Dsl.generic().name("A");

      GenericRenderable b = Dsl.generic().name("B").extends_(a);

      a = Dsl.generic().name("A").extends_(b);

      assertEquals("A extends B, B extends A", a.render(DEFAULT) + ", " + b.render(DEFAULT));
      //@end
   }
}
