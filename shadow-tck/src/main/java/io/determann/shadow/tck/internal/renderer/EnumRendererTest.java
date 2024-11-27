package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.type.C_Enum;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.GET_ENUM;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.test;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumRendererTest
{
   @Test
   void declaration()
   {
      //@formatter:off
      String expected = "@TestAnnotation\npublic enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {\ntest\n}\n";
      //@formatter:on
      withSource("EnumMultiParent.java", """
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
            .test(implementation ->
                  {

                     C_Enum cEnum = requestOrThrow(implementation, GET_ENUM, "EnumMultiParent");
                     assertEquals(expected, render(DEFAULT, cEnum).declaration("test"));
                  });
   }

   @Test
   void emptyDeclaration()
   {
      //@formatter:off
      String expected = "@TestAnnotation\npublic enum EnumMultiParent implements java.util.function.Consumer<EnumMultiParent>, java.util.function.Supplier<EnumMultiParent> {}\n";
      //@formatter:on
      withSource("EnumMultiParent.java", """
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
            .test(implementation ->
                  {

                     C_Enum cEnum = requestOrThrow(implementation, GET_ENUM, "EnumMultiParent");
                     assertEquals(expected, render(DEFAULT, cEnum).declaration());
                  });
   }

   @Test
   void invocation()
   {
      test(implementation ->
           {
              C_Enum cEnum = requestOrThrow(implementation, GET_ENUM, "java.lang.annotation.RetentionPolicy");
              assertEquals("java.lang.annotation.RetentionPolicy", render(DEFAULT, cEnum).type());
           });
   }
}