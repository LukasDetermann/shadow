package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

abstract class ShadowTest<SHADOW extends Shadow>
{
   private final Function<AnnotationProcessingContext, SHADOW> shadowSupplier;

   protected ShadowTest(Function<AnnotationProcessingContext, SHADOW> shadowSupplier) {this.shadowSupplier = shadowSupplier;}

   @Test
   void testRepresentsSameType()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertTrue(getShadowSupplier().apply(shadowApi).representsSameType(getShadowSupplier().apply(shadowApi)));
                               assertFalse(getShadowSupplier().apply(shadowApi)
                                                              .representsSameType(shadowApi.getClassOrThrow("java.util.jar.Attributes")));
                               assertFalse(getShadowSupplier().apply(shadowApi)
                                                              .representsSameType(shadowApi.getConstants().getUnboundWildcard()));
                            })
                   .withCodeToCompile("IntersectionExample.java",
                                      "public class IntersectionExample<T extends java.util.Collection & java.io.Serializable>{\n}")
                   .withCodeToCompile("ParameterExample.java", "                           public class ParameterExample\n" +
                                                               "                           {\n" +
                                                               "                              public ParameterExample(String name) {}\n" +
                                                               "\n" +
                                                               "                              public void foo(Long foo) { }\n" +
                                                               "                           }")
                   .withCodeToCompile("DefaultConstructorExample.java", "public class DefaultConstructorExample{}")
                   .withCodeToCompile("AnnotationUsageAnnotation.java",
                                      "                           import java.lang.annotation.ElementType;\n" +
                                      "                           import java.lang.annotation.Retention;\n" +
                                      "                           import java.lang.annotation.RetentionPolicy;\n" +
                                      "\n" +
                                      "                           public @interface AnnotationUsageAnnotation {\n" +
                                      "                              String stingValue() default \"string Value\";\n" +
                                      "                              boolean booleanValue() default false;\n" +
                                      "                              byte byteValue() default 1;\n" +
                                      "                              short shortValue() default 2;\n" +
                                      "                              int intValue() default 3;\n" +
                                      "                              long longValue() default 4L;\n" +
                                      "                              char charValue() default 'a';\n" +
                                      "                              float floatValue() default 5f;\n" +
                                      "                              double doubleValue() default 6D;\n" +
                                      "                              Class<String> typeValue() default String.class;\n" +
                                      "                              ElementType enumConstantValue() default ElementType.ANNOTATION_TYPE;\n" +
                                      "                              Retention annotationUsageValue() default @Retention(RetentionPolicy.CLASS);\n" +
                                      "                              char[] asListOfValues() default {'b', 'c'};\n" +
                                      "                           }")
                   .withCodeToCompile("AnnotationUsageExample.java", "                           @AnnotationUsageAnnotation\n" +
                                                                     "                           public class AnnotationUsageExample {\n" +
                                                                     "                              @AnnotationUsageAnnotation(stingValue = \"custom Value\")\n" +
                                                                     "                              private String testField;\n" +
                                                                     "                           }")
                   .compile();
   }

   @Test
   void testEquals()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(getShadowSupplier().apply(shadowApi), getShadowSupplier().apply(shadowApi));
                               assertNotEquals(getShadowSupplier().apply(shadowApi), shadowApi.getClassOrThrow("java.util.jar.Attributes"));
                            })
                   .withCodeToCompile("IntersectionExample.java",
                                      "public class IntersectionExample<T extends java.util.Collection & java.io.Serializable>{\n}")
                   .withCodeToCompile("ParameterExample.java", "                           public class ParameterExample\n" +
                                                               "                           {\n" +
                                                               "                              public ParameterExample(String name) {}\n" +
                                                               "\n" +
                                                               "                              public void foo(Long foo) { }\n" +
                                                               "                           }")
                   .withCodeToCompile("DefaultConstructorExample.java", "public class DefaultConstructorExample{}")
                   .withCodeToCompile("AnnotationUsageAnnotation.java",
                                      "                           import java.lang.annotation.ElementType;\n" +
                                      "                           import java.lang.annotation.Retention;\n" +
                                      "                           import java.lang.annotation.RetentionPolicy;\n" +
                                      "\n" +
                                      "                           public @interface AnnotationUsageAnnotation {\n" +
                                      "                              String stingValue() default \"string Value\";\n" +
                                      "                              boolean booleanValue() default false;\n" +
                                      "                              byte byteValue() default 1;\n" +
                                      "                              short shortValue() default 2;\n" +
                                      "                              int intValue() default 3;\n" +
                                      "                              long longValue() default 4L;\n" +
                                      "                              char charValue() default 'a';\n" +
                                      "                              float floatValue() default 5f;\n" +
                                      "                              double doubleValue() default 6D;\n" +
                                      "                              Class<String> typeValue() default String.class;\n" +
                                      "                              ElementType enumConstantValue() default ElementType.ANNOTATION_TYPE;\n" +
                                      "                              Retention annotationUsageValue() default @Retention(RetentionPolicy.CLASS);\n" +
                                      "                              char[] asListOfValues() default {'b', 'c'};\n" +
                                      "                           }")
                   .withCodeToCompile("AnnotationUsageExample.java", "                           @AnnotationUsageAnnotation\n" +
                                                                     "                           public class AnnotationUsageExample {\n" +
                                                                     "                              @AnnotationUsageAnnotation(stingValue = \"custom Value\")\n" +
                                                                     "                              private String testField;\n" +
                                                                     "                           }")
                   .compile();
   }

   @Test
   void testErasure()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(shadowApi.getInterfaceOrThrow("java.util.Collection"),
                                            shadowApi.withGenerics(shadowApi.getInterfaceOrThrow("java.util.Collection"), "java.lang.Object")
                                                     .erasure());

                               assertEquals(shadowApi.getInterfaceOrThrow("java.util.Collection"),
                                            shadowApi.withGenerics(shadowApi.getInterfaceOrThrow("java.util.Collection"), "java.lang.Object"));
                            })
                   .compile();
   }

   protected Function<AnnotationProcessingContext, SHADOW> getShadowSupplier()
   {
      return shadowSupplier;
   }
}
