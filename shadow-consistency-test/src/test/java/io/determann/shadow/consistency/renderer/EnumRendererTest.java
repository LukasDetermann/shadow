package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.shadow.type.C_Enum;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.<C_Enum>compileTime(context -> context.getEnumOrThrow("EnumMultiParent"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("EnumMultiParent")))
                     .withCode("EnumMultiParent.java", """
                           @TestAnnotation
                           public enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {
                                 ;
                                 @Override
                                 public void accept(EnumMultiParent enumMultiParent) {}

                                 @Override
                                 public EnumMultiParent get() {return null;}
                              }
                           """)
                     .withCode("TestAnnotation.java",
                               "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface TestAnnotation{}")
                     .test(aClass ->
                           {
                              assertEquals(
                                    "@TestAnnotation\npublic enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {}\n",
                                    render(DEFAULT, aClass).declaration());
                              assertEquals(
                                    "@TestAnnotation\npublic enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {\ntest\n}\n",
                                    render(DEFAULT, aClass).declaration("test"));
                           },
                           aClass ->
                           {
                              assertEquals(
                                    "@TestAnnotation\npublic enum EnumMultiParent implements java.util.function.Consumer, java.util.function.Supplier {}\n",
                                    render(DEFAULT, aClass).declaration());
                              assertEquals(
                                    "@TestAnnotation\npublic enum EnumMultiParent implements java.util.function.Consumer, java.util.function.Supplier {\ntest\n}\n",
                                    render(DEFAULT, aClass).declaration("test"));
                           });
   }

   @Test
   void type()
   {
      ConsistencyTest.<C_Enum>compileTime(context -> context.getEnumOrThrow("java.lang.annotation.RetentionPolicy"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("java.lang.annotation.RetentionPolicy")))
                     .test(aClass -> assertEquals("java.lang.annotation.RetentionPolicy", render(DEFAULT, aClass).type()));
   }
}