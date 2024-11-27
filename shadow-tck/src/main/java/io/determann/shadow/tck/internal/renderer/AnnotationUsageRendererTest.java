package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.tck.internal.TckTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationUsageRendererTest
{
   private TckTest.Builder testBuilder;

   @BeforeEach
   void setup()
   {
      testBuilder = withSource("AnnotationUsageAnnotation.java",
                               """
                                     import java.lang.annotation.ElementType;
                                     import java.lang.annotation.Retention;
                                     import java.lang.annotation.RetentionPolicy;
                                     
                                     @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
                                     public @interface AnnotationUsageAnnotation {
                                        String stingValue() default "string Value";
                                        boolean booleanValue() default false;
                                        byte byteValue() default 1;
                                        short shortValue() default 2;
                                        int intValue() default 3;
                                        long longValue() default 4L;
                                        char charValue() default 'a';
                                        float floatValue() default 5f;
                                        double doubleValue() default 6D;
                                        Class<String> typeValue() default String.class;
                                        ElementType enumConstantValue() default ElementType.ANNOTATION_TYPE;
                                        Retention annotationUsageValue() default @Retention(RetentionPolicy.CLASS);
                                        char[] asListOfValues() default {'b', 'c'};
                                     }
                                     """)
            .withSource("AnnotationUsageExample.java",
                        """
                              @AnnotationUsageAnnotation
                              public class AnnotationUsageExample {
                                 @AnnotationUsageAnnotation(stingValue = "custom Value")
                                 private String testField;
                              }
                              """);
   }

   @Test
   void emptyUsage()
   {
      //@formatter:off
      String expected = "@AnnotationUsageAnnotation(stingValue = \"string Value\", booleanValue = false, byteValue = 1, shortValue = 2, intValue = 3, longValue = 4L, charValue = 'a', floatValue = 5.0F, doubleValue = 6.0D, typeValue = String.class, enumConstantValue = java.lang.annotation.ElementType.ANNOTATION_TYPE, annotationUsageValue = @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.CLASS), asListOfValues = {'b', 'c'})";
      //@formatter:on
      testBuilder.test(implementation ->
                       {
                          C_Class annotationUsageExample = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                          C_AnnotationUsage usage = requestOrThrow(annotationUsageExample, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);
                          String actual = render(DEFAULT, usage).usage();
                          assertEquals(expected, actual);
                       });
   }

   @Test
   void overwriteEveryUsage()
   {
      //@formatter:off
      String expected = "@AnnotationUsageAnnotation(stingValue = test, booleanValue = test, byteValue = test, shortValue = test, intValue = test, longValue = test, charValue = test, floatValue = test, doubleValue = test, typeValue = test, enumConstantValue = test, annotationUsageValue = test, asListOfValues = test)";
      //@formatter:on

      testBuilder.test(implementation ->
                       {
                          C_Class annotationUsageExample = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                          C_AnnotationUsage usage = requestOrThrow(annotationUsageExample, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);
                          String actual = render(DEFAULT, usage).usage(method -> Optional.of("test"));
                          assertEquals(expected, actual);
                       });
   }

   @Test
   void overwriteSingleUsage()
   {
      //@formatter:off
      String expected = "@AnnotationUsageAnnotation(stingValue = \"test\", booleanValue = false, byteValue = 1, shortValue = 2, intValue = 3, longValue = 4L, charValue = 'a', floatValue = 5.0F, doubleValue = 6.0D, typeValue = String.class, enumConstantValue = java.lang.annotation.ElementType.ANNOTATION_TYPE, annotationUsageValue = @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.CLASS), asListOfValues = {'b', 'c'})";
      //@formatter:on

      testBuilder.test(implementation ->
                       {
                          C_Class annotationUsageExample = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                          C_AnnotationUsage usage = requestOrThrow(annotationUsageExample, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);
                          String actual = render(DEFAULT, usage)
                                .usage(method ->
                                       {
                                          C_Type returnType = requestOrThrow(method, EXECUTABLE_GET_RETURN_TYPE);
                                          if (!(returnType instanceof C_Class aClass))
                                          {
                                             return Optional.empty();
                                          }
                                          if (requestOrThrow(aClass, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals("java.lang.String"))
                                          {
                                             return Optional.of("\"test\"");
                                          }
                                          return Optional.empty();
                                       });
                          assertEquals(expected, actual);
                       });
   }
}