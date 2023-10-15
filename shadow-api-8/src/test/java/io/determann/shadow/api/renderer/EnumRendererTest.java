package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumRendererTest
{

   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(
                                     "@TestAnnotation\npublic enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {}\n",
                                     render(DEFAULT, shadowApi.getEnumOrThrow("EnumMultiParent")).declaration());
                               assertEquals(
                                     "@TestAnnotation\npublic enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {\ntest\n}\n",
                                     render(DEFAULT, shadowApi.getEnumOrThrow("EnumMultiParent")).declaration("test"));
                            })
                   .withCodeToCompile("EnumMultiParent.java", "@TestAnnotation\n" +
                                                              "public enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {\n" +
                                                              "      ;\n" +
                                                              "      @Override\n" +
                                                              "      public void accept(EnumMultiParent enumMultiParent) {}\n" +
                                                              "\n" +
                                                              "      @Override\n" +
                                                              "      public EnumMultiParent get() {return null;}\n" +
                                                              "   }\n")
                   .withCodeToCompile("TestAnnotation.java", "@interface TestAnnotation{}")
                   .compile();
   }

   @Test
   void type()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals("java.lang.annotation.RetentionPolicy",
                                               render(DEFAULT, shadowApi.getEnumOrThrow("java.lang.annotation.RetentionPolicy")).type()))
                   .compile();
   }
}