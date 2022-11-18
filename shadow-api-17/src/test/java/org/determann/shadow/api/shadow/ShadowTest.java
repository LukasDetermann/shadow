package org.determann.shadow.api.shadow;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import javax.lang.model.type.TypeMirror;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

abstract class ShadowTest<SHADOW extends Shadow<? extends TypeMirror>>
{
   private final Function<ShadowApi, SHADOW> shadowSupplier;

   protected ShadowTest(Function<ShadowApi, SHADOW> shadowSupplier) {this.shadowSupplier = shadowSupplier;}

   @Test
   void testRepresentsSameType()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertTrue(getShadowSupplier().apply(shadowApi).representsSameType(getShadowSupplier().apply(shadowApi)));
                                 assertFalse(getShadowSupplier().apply(shadowApi)
                                                                .representsSameType(shadowApi.getClassOrThrow("java.util.jar.Attributes")));
                                 assertFalse(getShadowSupplier().apply(shadowApi)
                                                                .representsSameType(shadowApi.getConstants().getUnboundWildcard()));
                              })
                     .withCodeToCompile("RecordExample.java", "public record RecordExample(Long id) implements java.io.Serializable{}")
                     .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                     .withCodeToCompile("IntersectionExample.java",
                                        "public class IntersectionExample<T extends java.util.Collection & java.io.Serializable>{\n}")
                     .withCodeToCompile("ParameterExample.java", """
                           public class ParameterExample
                           {
                              public ParameterExample(String name) {}

                              public void foo(Long foo) { }
                           }
                           """)
                     .withCodeToCompile("DefaultConstructorExample.java", "public class DefaultConstructorExample{}")
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

   @Test
   void testEquals()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(getShadowSupplier().apply(shadowApi), getShadowSupplier().apply(shadowApi));
                                 assertNotEquals(getShadowSupplier().apply(shadowApi), shadowApi.getClassOrThrow("java.util.jar.Attributes"));
                              })
                     .withCodeToCompile("RecordExample.java", "public record RecordExample(Long id) implements java.io.Serializable{}")
                     .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                     .withCodeToCompile("IntersectionExample.java",
                                        "public class IntersectionExample<T extends java.util.Collection & java.io.Serializable>{\n}")
                     .withCodeToCompile("ParameterExample.java", """
                           public class ParameterExample
                           {
                              public ParameterExample(String name) {}

                              public void foo(Long foo) { }
                           }
                           """)
                     .withCodeToCompile("DefaultConstructorExample.java", "public class DefaultConstructorExample{}")
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

   @Test
   void testErasure()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(shadowApi.getInterfaceOrThrow("java.util.Collection"),
                                              shadowApi.getInterfaceOrThrow("java.util.Collection").withGenerics("java.lang.Object").erasure());

                                 assertEquals(shadowApi.getInterfaceOrThrow("java.util.Collection"),
                                              shadowApi.getInterfaceOrThrow("java.util.Collection").withGenerics("java.lang.Object"));
                              })
                     .compile();
   }

   protected Function<ShadowApi, SHADOW> getShadowSupplier()
   {
      return shadowSupplier;
   }
}
