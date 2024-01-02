package io.determann.shadow.api.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.consistency.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.compileTime(context -> context.getAnnotationOrThrow("java.lang.annotation.Retention"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("java.lang.annotation.Retention")))
                     .test(annotation -> assertEquals("""
                                                  @java.lang.annotation.Documented
                                                  @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                                  @java.lang.annotation.Target(value = {java.lang.annotation.ElementType.ANNOTATION_TYPE})
                                                  public @interface Retention {}
                                                  """,
                                                  render(RenderingContext.DEFAULT, annotation).declaration()));

      ConsistencyTest.compileTime(context -> context.getAnnotationOrThrow("java.lang.annotation.Retention"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("java.lang.annotation.Retention")))
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
      ConsistencyTest.compileTime(context -> context.getAnnotationOrThrow("java.lang.annotation.Retention"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("java.lang.annotation.Retention")))
                     .test(annotation -> assertEquals("java.lang.annotation.Retention", render(RenderingContext.DEFAULT, annotation).type()));
   }

   @Test
   void typ2e()
   {
      ConsistencyTest.compileTime(context -> context.getClassOrThrow("Test23"))
                     .withCode("Test", "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME) @interface Test\n" +
                                       "   {\n" +
                                       "      String since() default \"\";\n" +
                                       "   }")
            .withCode("Test23",  "@Test class Test23{}")
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("Test23")))
                     .test(annotation -> render(RenderingContext.DEFAULT, annotation).declaration());
   }
}