package io.determann.shadow.annotation_processing.dsl;

import io.determann.shadow.api.annotation_processing.dsl.JavaDsl;
import io.determann.shadow.api.annotation_processing.dsl.generic.GenericRenderable;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.annotation_processing.dsl.RenderingContext.createRenderingContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericDslTest
{
   @Test
   void annotate()
   {
      assertEquals("@MyAnnotation1 @MyAnnotation2 T",
                   JavaDsl.generic()
                          .annotate("MyAnnotation1")
                          .annotate(JavaDsl.annotationUsage().type("MyAnnotation2"))
                          .name("T")
                          .renderDeclaration(createRenderingContext()));
   }

   @Test
   void extends_()
   {
      assertEquals("T extends org.example.MyClass & MyInterface & org.example.MyInterface2",
                   JavaDsl.generic()
                          .name("T")
                          .extends_(JavaDsl.class_().package_("org.example").name("MyClass"))
                          .extends_("MyInterface")
                          .extends_(JavaDsl.interface_().package_("org.example").name("MyInterface2"))
                          .renderDeclaration(createRenderingContext()));
   }

   @Test
   void nested()
   {
      GenericRenderable bound = JavaDsl.generic()
                                       .name("A")
                                       .extends_("MyClass");

      assertEquals("B extends A",
                   JavaDsl.generic()
                          .name("B")
                          .extends_(bound)
                          .renderDeclaration(createRenderingContext()));
   }

   @Test
   void usage()
   {
      GenericRenderable generic = JavaDsl.generic().name("T").extends_("MyClass");

      assertEquals("T t", JavaDsl.parameter().type(generic).name("t").renderDeclaration(createRenderingContext()));
   }

   @Test
   void api()
   {
      //@start region="api"
      assertEquals("T extends MyClass", JavaDsl.generic().name("T").extends_("MyClass").renderDeclaration(createRenderingContext()));
      //@end
   }

   @Test
   void recursive()
   {
      //@start region="recursive"
      GenericRenderable a = JavaDsl.generic().name("A");

      GenericRenderable b = JavaDsl.generic().name("B").extends_(a);

      a = JavaDsl.generic().name("A").extends_(b);

      assertEquals("A extends B, B extends A", a.renderDeclaration(createRenderingContext()) + ", " + b.renderDeclaration(createRenderingContext()));
      //@end
   }
}
