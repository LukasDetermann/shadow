package io.determann.shadow.api.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.consistency.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.compileTime(context -> context.getClassOrThrow("AnnotatedField"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("AnnotatedField")))
                     .withCode("MyAnnotation.java",
                               "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface MyAnnotation {} ")
                     .withCode("AnnotatedField.java",
                               "class AnnotatedField { @MyAnnotation private final String value = \"\";}")
                     .test(aClass -> assertEquals("@MyAnnotation\nprivate final String value;\n",
                                                  render(DEFAULT, aClass.getFieldOrThrow("value"))
                                                        .declaration()));
   }
}