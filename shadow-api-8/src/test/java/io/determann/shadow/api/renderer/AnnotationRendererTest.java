package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.Annotation;
import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationRendererTest
{

   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Annotation annotation = shadowApi.getAnnotationOrThrow("java.lang.annotation.Retention");
                               assertEquals("@java.lang.annotation.Documented\n" +
                                            "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n" +
                                            "@java.lang.annotation.Target(value = {java.lang.annotation.ElementType.ANNOTATION_TYPE})\n" +
                                            "public @interface Retention {}\n",
                                            render(DEFAULT, annotation).declaration());
                               assertEquals("@java.lang.annotation.Documented\n" +
                                            "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n" +
                                            "@java.lang.annotation.Target(value = {java.lang.annotation.ElementType.ANNOTATION_TYPE})\n" +
                                            "public @interface Retention {\n" +
                                            "test\n" +
                                            "}\n",
                                            render(DEFAULT, annotation).declaration("test"));
                            })
                   .compile();
   }

   @Test
   void type()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Annotation annotation = shadowApi.getAnnotationOrThrow("java.lang.annotation.Retention");
                               assertEquals("java.lang.annotation.Retention", render(DEFAULT, annotation).type());
                            })
                   .compile();
   }
}