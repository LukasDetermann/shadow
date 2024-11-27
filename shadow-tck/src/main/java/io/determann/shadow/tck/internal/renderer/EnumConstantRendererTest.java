package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.structure.C_EnumConstant;
import io.determann.shadow.api.shadow.type.C_Enum;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.ENUM_GET_ENUM_CONSTANT;
import static io.determann.shadow.api.Operations.GET_ENUM;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.test;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumConstantRendererTest
{
   @Test
   void emptyDeclaration()
   {
      test(implementation ->
           {
              C_Enum cEnum = requestOrThrow(implementation, GET_ENUM, "java.lang.annotation.RetentionPolicy");
              C_EnumConstant enumConstant = requestOrThrow(cEnum, ENUM_GET_ENUM_CONSTANT, "SOURCE");
              assertEquals("SOURCE\n", render(DEFAULT, enumConstant).declaration());
           });
   }

   @Test
   void declarationWithParameter()
   {
      test(implementation ->
           {
              C_Enum cEnum = requestOrThrow(implementation, GET_ENUM, "java.lang.annotation.RetentionPolicy");
              C_EnumConstant enumConstant = requestOrThrow(cEnum, ENUM_GET_ENUM_CONSTANT, "SOURCE");
              assertEquals("SOURCE(test)\n", render(DEFAULT, enumConstant).declaration("test"));
           });
   }

   @Test
   void declarationWithContent()
   {
      test(implementation ->
           {
              C_Enum cEnum = requestOrThrow(implementation, GET_ENUM, "java.lang.annotation.RetentionPolicy");
              C_EnumConstant enumConstant = requestOrThrow(cEnum, ENUM_GET_ENUM_CONSTANT, "SOURCE");
              assertEquals("SOURCE(test) {\ntest2\n}\n", render(DEFAULT, enumConstant).declaration("test", "test2"));
           });
   }

   @Test
   void annotatedDeclaration()
   {
      withSource("AnnotatedEnumConstant.java",
                 """
                       enum AnnotatedEnumConstant{
                       @TestAnnotation TEST
                       }""")
            .withSource("AnnotatedEnumConstant",
                        "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface TestAnnotation{}")
            .test(implementation ->
                  {
                     C_Enum cEnum = requestOrThrow(implementation, GET_ENUM, "AnnotatedEnumConstant");
                     C_EnumConstant enumConstant = requestOrThrow(cEnum, ENUM_GET_ENUM_CONSTANT, "TEST");
                     assertEquals("@TestAnnotation\nTEST\n", render(DEFAULT, enumConstant).declaration());
                  });
   }

   @Test
   void invocation()
   {
      test(implementation ->
           {
              C_Enum cEnum = requestOrThrow(implementation, GET_ENUM, "java.lang.annotation.RetentionPolicy");
              C_EnumConstant enumConstant = requestOrThrow(cEnum, ENUM_GET_ENUM_CONSTANT, "SOURCE");
              assertEquals("java.lang.annotation.RetentionPolicy.SOURCE", render(DEFAULT, enumConstant).invocation());
           });
   }
}