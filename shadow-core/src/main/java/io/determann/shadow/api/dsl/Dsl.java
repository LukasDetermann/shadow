package io.determann.shadow.api.dsl;

import io.determann.shadow.api.dsl.class_.ClassJavaDocStep;
import io.determann.shadow.api.dsl.constructor.ConstructorJavaDocStep;
import io.determann.shadow.api.dsl.enum_constant.EnumConstantJavaDocStep;
import io.determann.shadow.api.dsl.method.MethodJavaDocStep;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.internal.dsl.ClassDsl;
import io.determann.shadow.internal.dsl.ConstructorDsl;
import io.determann.shadow.internal.dsl.EnumConstantDsl;
import io.determann.shadow.internal.dsl.MethodDsl;

public interface Dsl
{
   static ConstructorJavaDocStep constructor()
   {
      return new ConstructorDsl();
   }

   static MethodJavaDocStep method()
   {
      return new MethodDsl();
   }

   static ClassJavaDocStep class_()
   {
      return new ClassDsl();
   }

   static EnumConstantJavaDocStep enumConstant()
   {
      return new EnumConstantDsl();
   }

   class ExampleWrapper
   {
      static
      {
         constructor().javadoc("my doku")
                      .annotate("@AnAnnotation")
                      .public_()
                      .generic("<T>")
                      .type("MyType")
                      .parameter("String s")
                      .parameter("int i")
                      .throws_("IllegalStateException")
                      .body("throw null")
                      .render(RenderingContext.DEFAULT);
      }
   }
}
