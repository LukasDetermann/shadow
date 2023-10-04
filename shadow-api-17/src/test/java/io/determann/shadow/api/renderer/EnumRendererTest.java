package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.ShadowApi.render;
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
                                     render(shadowApi.getEnumOrThrow("EnumMultiParent")).declaration());
                               assertEquals(
                                     "@TestAnnotation\npublic enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {\ntest\n}\n",
                                     render(shadowApi.getEnumOrThrow("EnumMultiParent")).declaration("test"));
                            })
                   .withCodeToCompile("EnumMultiParent.java", """
                         @TestAnnotation
                         public enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {
                               ;
                               @Override
                               public void accept(EnumMultiParent enumMultiParent) {}

                               @Override
                               public EnumMultiParent get() {return null;}
                            }
                         """)
                   .withCodeToCompile("TestAnnotation.java", "@interface TestAnnotation{}")
                   .compile();
   }

   @Test
   void type()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals("java.lang.annotation.RetentionPolicy",
                                               render(shadowApi.getEnumOrThrow("java.lang.annotation.RetentionPolicy")).type()))
                   .compile();
   }
}