package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.type.C_Enum;
import io.determann.shadow.tck.internal.RenderingTestBuilder;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.RenderingTestBuilder.renderingTest;

class EnumRendererTest
{
   @Test
   void declaration()
   {
      RenderingTestBuilder<C_Enum> multiTest = renderingTest(C_Enum.class)
            .withSource("EnumMultiParent.java", """
                  @TestAnnotation
                  public enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {
                        ;
                        @Override
                        public void accept(EnumMultiParent enumMultiParent) {}
                  
                        @Override
                        public EnumMultiParent get() {return null;}
                     }
                  """)
            .withSource("TestAnnotation.java",
                        "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)\n@interface TestAnnotation{}")
            .withToRender("EnumMultiParent");

      multiTest.withRender(cEnum -> render(DEFAULT, cEnum).declaration())
               .withExpected(
                     "@TestAnnotation\npublic enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {}\n")
               .test();

      multiTest.withRender(cEnum -> render(DEFAULT, cEnum).declaration("test"))
               .withExpected(
                     "@TestAnnotation\npublic enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {\ntest\n}\n")
               .test();

   }

   @Test
   void type()
   {
      renderingTest(C_Enum.class).withToRender("java.lang.annotation.RetentionPolicy")
                                 .withRender(cEnum -> render(DEFAULT, cEnum).type())
                                 .withExpected("java.lang.annotation.RetentionPolicy")
                                 .test();
   }
}