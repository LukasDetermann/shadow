package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.structure.C_EnumConstant;
import io.determann.shadow.api.shadow.type.C_Enum;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.ENUM_GET_ENUM_CONSTANT;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.RenderingTestBuilder.renderingTest;

class EnumConstantRendererTest
{
   @Test
   void declaration()
   {
      renderingTest(C_Enum.class).withToRender("java.lang.annotation.RetentionPolicy")
                                 .withRender(cEnum ->
                                             {
                                                C_EnumConstant enumConstant = requestOrThrow(cEnum, ENUM_GET_ENUM_CONSTANT, "SOURCE");
                                                return render(DEFAULT, enumConstant).declaration();
                                             })
                                 .withExpected("SOURCE\n")
                                 .test();

      renderingTest(C_Enum.class).withToRender("java.lang.annotation.RetentionPolicy")
                                 .withRender(cEnum ->
                                             {
                                                C_EnumConstant enumConstant = requestOrThrow(cEnum, ENUM_GET_ENUM_CONSTANT, "SOURCE");
                                                return render(DEFAULT, enumConstant).declaration("test");
                                             })
                                 .withExpected("SOURCE(test)\n")
                                 .test();

      renderingTest(C_Enum.class).withToRender("java.lang.annotation.RetentionPolicy")
                                 .withRender(cEnum ->
                                             {
                                                C_EnumConstant enumConstant = requestOrThrow(cEnum, ENUM_GET_ENUM_CONSTANT, "SOURCE");
                                                return render(DEFAULT, enumConstant).declaration("test", "test2");
                                             })
                                 .withExpected("SOURCE(test) {\ntest2\n}\n")
                                 .test();

      renderingTest(C_Enum.class).withSource("AnnotatedEnumConstant.java",
                                             """
                                                   enum AnnotatedEnumConstant{
                                                   @TestAnnotation TEST
                                                   }""")
                                 .withSource("AnnotatedEnumConstant",
                                             "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface TestAnnotation{}")
                                 .withToRender("AnnotatedEnumConstant")
                                 .withRender(cEnum ->
                                             {
                                                C_EnumConstant enumConstant = requestOrThrow(cEnum, ENUM_GET_ENUM_CONSTANT, "TEST");
                                                return render(DEFAULT, enumConstant).declaration();
                                             })
                                 .withExpected("@TestAnnotation\nTEST\n")
                                 .test();
   }

   @Test
   void invocation()
   {
      renderingTest(C_Enum.class).withToRender("java.lang.annotation.RetentionPolicy")
                                 .withRender(cEnum ->
                                             {
                                                C_EnumConstant enumConstant = requestOrThrow(cEnum, ENUM_GET_ENUM_CONSTANT, "SOURCE");
                                                return render(DEFAULT, enumConstant).invocation();
                                             })
                                 .withExpected("java.lang.annotation.RetentionPolicy.SOURCE")
                                 .test();
   }
}