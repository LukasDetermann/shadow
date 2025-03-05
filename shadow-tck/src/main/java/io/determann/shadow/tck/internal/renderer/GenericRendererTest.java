package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Generic;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.CLASS_GET_GENERICS;
import static io.determann.shadow.api.Operations.GET_CLASS;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericRendererTest
{
   @Test
   void declaration()
   {
      withSource("MyAnnotation.java",
                 "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME) @java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE_USE) @interface MyAnnotation {} ")
            .withSource("Annotated.java", "class Annotated<@MyAnnotation T> {}")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Annotated");
                     C_Generic generic = requestOrThrow(cClass, CLASS_GET_GENERICS).get(0);
                     assertEquals("@MyAnnotation T", render(generic).declaration(DEFAULT));
                  });
   }

   @Test
   void type()
   {
      withSource("Annotated.java", "class Annotated<T> {} ")
            .test(implementation ->
                  {
                     C_Class cClass = requestOrThrow(implementation, GET_CLASS, "Annotated");
                     C_Generic generic = requestOrThrow(cClass, CLASS_GET_GENERICS).get(0);
                     assertEquals("T", render(generic).declaration(DEFAULT));
                  });
   }
}