package io.determann.shadow.api.dsl;

import io.determann.shadow.api.dsl.constructor.ConstructorJavaDocStep;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.internal.dsl.ConstructorDsl;

public interface Dsl
{
   static ConstructorJavaDocStep constructor()
   {
      return new ConstructorDsl();
   }

   class ExampleWrapper
   {
      static
      {
         constructor().javadoc("my doku")
                      .annotate("@AnAnnotation")
                      .public_()
                      .generic("<T>")
                      .constructor("MyType")
                      .parameter("String s")
                      .parameter("int i")
                      .throws_("IllegalStateException")
                      .body("throw null")
                      .render(RenderingContext.DEFAULT);
      }
   }
}
