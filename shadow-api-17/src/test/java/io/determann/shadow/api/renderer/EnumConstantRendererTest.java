package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.EnumConstant;
import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumConstantRendererTest
{

   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               EnumConstant constant = shadowApi.getEnumOrThrow("java.lang.annotation.RetentionPolicy")
                                                                .getEnumConstantOrThrow("SOURCE");

                               assertEquals("SOURCE\n", render(DEFAULT, constant).declaration());
                               assertEquals("SOURCE(test)\n", render(DEFAULT, constant).declaration("test"));
                               assertEquals("SOURCE(test) {\ntest2\n}\n", render(DEFAULT, constant).declaration("test", "test2"));

                               EnumConstant constant1 = shadowApi.getEnumOrThrow("AnnotatedEnumConstant").getEnumConstantOrThrow("TEST");
                               assertEquals("@TestAnnotation\nTEST\n", render(DEFAULT, constant1).declaration());
                            })
                   .withCodeToCompile("AnnotatedEnumConstant.java",
                                      """
                                            enum AnnotatedEnumConstant{
                                            @TestAnnotation TEST
                                            }""")
                   .withCodeToCompile("TestAnnotation.java",
                                      "@interface TestAnnotation{}")
                   .compile();
   }

   @Test
   void invocation()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               EnumConstant constant = shadowApi.getEnumOrThrow("java.lang.annotation.RetentionPolicy")
                                                                .getEnumConstantOrThrow("SOURCE");
                               assertEquals("java.lang.annotation.RetentionPolicy.SOURCE", render(DEFAULT, constant).invocation());
                            })
                   .compile();
   }
}