package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.type.C_Annotation;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.GET_ANNOTATION;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationRendererTest
{
   @Test
   void declaration()
   {
      String expected = """
            @java.lang.annotation.Documented
            @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
            @java.lang.annotation.Target(value = {java.lang.annotation.ElementType.ANNOTATION_TYPE})
            public @interface Retention {
            test
            }
            """;
      test(implementation ->
           {
              C_Annotation retention = requestOrThrow(implementation, GET_ANNOTATION, "java.lang.annotation.Retention");

              String actual = render(DEFAULT, retention).declaration("test");

              assertEquals(expected, actual);
           });
   }

   @Test
   void emptyDeclaration()
   {
      String expected = """
            @java.lang.annotation.Documented
            @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
            @java.lang.annotation.Target(value = {java.lang.annotation.ElementType.ANNOTATION_TYPE})
            public @interface Retention {}
            """;
      test(implementation ->
           {
              C_Annotation retention = requestOrThrow(implementation, GET_ANNOTATION, "java.lang.annotation.Retention");

              String actual = render(DEFAULT, retention).declaration();

              assertEquals(expected, actual);
           });
   }

   @Test
   void type()
   {
      test(implementation ->
           {
              C_Annotation retention = requestOrThrow(implementation, GET_ANNOTATION, "java.lang.annotation.Retention");

              String actual = render(DEFAULT, retention).type();

              assertEquals("java.lang.annotation.Retention", actual);
           });
   }
}