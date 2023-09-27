package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.determann.shadow.api.ShadowApi.render;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationUsageRendererTest
{
   @Test
   void usage()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               AnnotationUsage annotationUsage = shadowApi.getClassOrThrow("AnnotationUsageExample")
                                                                       .getAnnotationUsages()
                                                                       .get(0);

                               assertEquals(
                                     "@AnnotationUsageAnnotation(stingValue = \"string Value\", booleanValue = false, byteValue = 1, shortValue = 2, intValue = 3, longValue = 4L, charValue = 'a', floatValue = 5.0F, doubleValue = 6.0D, typeValue = String.class, enumConstantValue = java.lang.annotation.ElementType.ANNOTATION_TYPE, annotationUsageValue = @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.CLASS), asListOfValues = {'b', 'c'})",
                                     render(annotationUsage).usage());

                               assertEquals(
                                     "@AnnotationUsageAnnotation(stingValue = test, booleanValue = test, byteValue = test, shortValue = test, intValue = test, longValue = test, charValue = test, floatValue = test, doubleValue = test, typeValue = test, enumConstantValue = test, annotationUsageValue = test, asListOfValues = test)",
                                     render(annotationUsage).usage(method -> Optional.of("test")));

                               assertEquals(
                                     "@AnnotationUsageAnnotation(stingValue = \"test\", booleanValue = false, byteValue = 1, shortValue = 2, intValue = 3, longValue = 4L, charValue = 'a', floatValue = 5.0F, doubleValue = 6.0D, typeValue = String.class, enumConstantValue = java.lang.annotation.ElementType.ANNOTATION_TYPE, annotationUsageValue = @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.CLASS), asListOfValues = {'b', 'c'})",
                                     render(annotationUsage).usage(method ->
                                                                             {
                                                                                if (method.getReturnType().representsSameType(shadowApi.getClassOrThrow("java.lang.String")))
                                                                                {
                                                                                   return Optional.of("\"test\"");
                                                                                }
                                                                                return Optional.empty();
                                                                             }));
                            })
                   .withCodeToCompile("AnnotationUsageAnnotation.java", """
                           import java.lang.annotation.ElementType;
                           import java.lang.annotation.Retention;
                           import java.lang.annotation.RetentionPolicy;

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
                   .withCodeToCompile("AnnotationUsageExample.java", """
                           @AnnotationUsageAnnotation
                           public class AnnotationUsageExample {
                              @AnnotationUsageAnnotation(stingValue = "custom Value")
                              private String testField;
                           }
                           """)
                   .compile();
   }
}