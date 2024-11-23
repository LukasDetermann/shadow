package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.type.C_Annotation;
import io.determann.shadow.tck.internal.RenderingTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.RenderingTestBuilder.renderingTest;

class AnnotationRendererTest
{
   private RenderingTestBuilder<C_Annotation> renderingTest;

   @BeforeEach
   void setup()
   {
      renderingTest = renderingTest(C_Annotation.class).withToRender("java.lang.annotation.Retention");
   }

   @Test
   void declaration()
   {
      renderingTest.withRender(annotation -> render(DEFAULT, annotation).declaration("test"))
                   .withExpected("""
                                       @java.lang.annotation.Documented
                                       @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                       @java.lang.annotation.Target(value = {java.lang.annotation.ElementType.ANNOTATION_TYPE})
                                       public @interface Retention {
                                       test
                                       }
                                       """)
                   .test();
   }

   @Test
   void emptyDeclaration()
   {
      renderingTest.withRender(annotation -> render(DEFAULT, annotation).declaration())
                   .withExpected("""
                                       @java.lang.annotation.Documented
                                       @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                       @java.lang.annotation.Target(value = {java.lang.annotation.ElementType.ANNOTATION_TYPE})
                                       public @interface Retention {}
                                       """)
                   .test();
   }

   @Test
   void type()
   {
      renderingTest.withRender(annotation -> render(DEFAULT, annotation).type())
                   .withExpected("java.lang.annotation.Retention")
                   .test();
   }
}