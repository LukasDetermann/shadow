package io.determann.shadow.api.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.EnumConstant;
import io.determann.shadow.consistency.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumConstantRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.compileTime(context -> context.getEnumOrThrow("java.lang.annotation.RetentionPolicy"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("java.lang.annotation.RetentionPolicy")))
                     .test(aClass ->
                           {
                              EnumConstant constant = aClass.getEnumConstantOrThrow("SOURCE");
                              assertEquals("SOURCE\n", render(DEFAULT, constant).declaration());
                              assertEquals("SOURCE(test)\n", render(DEFAULT, constant).declaration("test"));
                              assertEquals("SOURCE(test) {\ntest2\n}\n", render(DEFAULT, constant).declaration("test", "test2"));
                           });

      ConsistencyTest.compileTime(context -> context.getEnumOrThrow("AnnotatedEnumConstant"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("AnnotatedEnumConstant")))
                     .withCode("AnnotatedEnumConstant.java",
                               """
                                     enum AnnotatedEnumConstant{
                                     @TestAnnotation TEST
                                     }""")
                     .withCode("TestAnnotation.java",
                               "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface TestAnnotation{}")
                     .test(aClass -> assertEquals("@TestAnnotation\nTEST\n",
                                                  render(DEFAULT, aClass.getEnumConstantOrThrow("TEST")).declaration()));
   }

   @Test
   void invocation()
   {
      ConsistencyTest.compileTime(context -> context.getEnumOrThrow("java.lang.annotation.RetentionPolicy"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("java.lang.annotation.RetentionPolicy")))
                     .test(aClass -> assertEquals("java.lang.annotation.RetentionPolicy.SOURCE", render(DEFAULT, aClass.getEnumConstantOrThrow("SOURCE")).invocation()));
   }
}