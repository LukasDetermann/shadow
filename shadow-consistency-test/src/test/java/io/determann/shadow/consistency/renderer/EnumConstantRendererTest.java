package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.shadow.structure.C_EnumConstant;
import io.determann.shadow.api.shadow.type.C_Enum;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.ENUM_GET_ENUM_CONSTANT;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumConstantRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.<C_Enum>compileTime(context -> context.getEnumOrThrow("java.lang.annotation.RetentionPolicy"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("java.lang.annotation.RetentionPolicy")))
                     .test(aClass ->
                           {
                              C_EnumConstant constant = requestOrThrow(aClass, ENUM_GET_ENUM_CONSTANT, "SOURCE");
                              assertEquals("SOURCE\n", render(DEFAULT, constant).declaration());
                              assertEquals("SOURCE(test)\n", render(DEFAULT, constant).declaration("test"));
                              assertEquals("SOURCE(test) {\ntest2\n}\n", render(DEFAULT, constant).declaration("test", "test2"));
                           });

      ConsistencyTest.<C_Enum>compileTime(context -> context.getEnumOrThrow("AnnotatedEnumConstant"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("AnnotatedEnumConstant")))
                     .withCode("AnnotatedEnumConstant.java",
                               """
                                     enum AnnotatedEnumConstant{
                                     @TestAnnotation TEST
                                     }""")
                     .withCode("TestAnnotation.java",
                               "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface TestAnnotation{}")
                     .test(aClass -> assertEquals("@TestAnnotation\nTEST\n",
                                                  render(DEFAULT, requestOrThrow(aClass, ENUM_GET_ENUM_CONSTANT, "TEST")).declaration()));
   }

   @Test
   void invocation()
   {
      ConsistencyTest.<C_Enum>compileTime(context -> context.getEnumOrThrow("java.lang.annotation.RetentionPolicy"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("java.lang.annotation.RetentionPolicy")))
                     .test(aClass -> assertEquals("java.lang.annotation.RetentionPolicy.SOURCE", render(DEFAULT, requestOrThrow(aClass, ENUM_GET_ENUM_CONSTANT, "SOURCE")).invocation()));
   }
}