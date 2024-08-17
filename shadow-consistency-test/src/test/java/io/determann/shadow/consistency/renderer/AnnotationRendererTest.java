package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.Annotation;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.<Annotation>compileTime(context -> context.getAnnotationOrThrow("java.lang.annotation.Retention"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("java.lang.annotation.Retention")))
                     .test(annotation -> Assertions.assertEquals("""
                                                  @java.lang.annotation.Documented
                                                  @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                                  @java.lang.annotation.Target(value = {java.lang.annotation.ElementType.ANNOTATION_TYPE})
                                                  public @interface Retention {}
                                                  """,
                                                                 Renderer.render(RenderingContext.DEFAULT, annotation).declaration()));

      ConsistencyTest.<Annotation>compileTime(context -> context.getAnnotationOrThrow("java.lang.annotation.Retention"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("java.lang.annotation.Retention")))
                     .test(annotation -> assertEquals("""
                                                  @java.lang.annotation.Documented
                                                  @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                                  @java.lang.annotation.Target(value = {java.lang.annotation.ElementType.ANNOTATION_TYPE})
                                                  public @interface Retention {
                                                  test
                                                  }
                                                  """,
                                                      render(RenderingContext.DEFAULT, annotation).declaration("test")));
   }

   @Test
   void type()
   {
      ConsistencyTest.<Annotation>compileTime(context -> context.getAnnotationOrThrow("java.lang.annotation.Retention"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("java.lang.annotation.Retention")))
                     .test(annotation -> assertEquals("java.lang.annotation.Retention", render(RenderingContext.DEFAULT, annotation).type()));
   }

   @Test
   void typ2e()
   {
      ConsistencyTest.<Class>compileTime(context -> context.getClassOrThrow("Test23"))
                     .withCode("Test", "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME) @interface Test\n" +
                                       "   {\n" +
                                       "      String since() default \"\";\n" +
                                       "   }")
            .withCode("Test23",  "@Test class Test23{}")
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("Test23")))
                     .test(annotation -> render(RenderingContext.DEFAULT, annotation).declaration());
   }
}