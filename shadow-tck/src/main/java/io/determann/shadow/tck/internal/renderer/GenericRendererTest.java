package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Generic;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.CLASS_GET_GENERICS;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.RenderingTestBuilder.renderingTest;

class GenericRendererTest
{
   @Test
   void declaration()
   {
      renderingTest(C_Class.class).withSource("MyAnnotation.java",
                                              "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME) @java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE_USE) @interface MyAnnotation {} ")
                                  .withSource("Annotated.java", "class Annotated<@MyAnnotation T> {}")
                                  .withToRender("Annotated")
                                  .withRender(cClass ->
                                              {
                                                 C_Generic generic = requestOrThrow(cClass, CLASS_GET_GENERICS).get(0);
                                                 return render(DEFAULT, generic).declaration();
                                              })
                                  .withExpected("@MyAnnotation T")
                                  .test();
   }

   @Test
   void type()
   {
      renderingTest(C_Class.class).withSource("Annotated.java", "class Annotated<T> {} ")
                                  .withToRender("Annotated")
                                  .withRender(cClass ->
                                              {
                                                 C_Generic generic = requestOrThrow(cClass, CLASS_GET_GENERICS).get(0);
                                                 return render(DEFAULT, generic).type();
                                              })
                                  .withExpected("T")
                                  .test();
   }
}