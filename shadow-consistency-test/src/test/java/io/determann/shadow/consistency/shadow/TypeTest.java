package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.AP_Context;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.shadow.type.C_Type;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static io.determann.shadow.api.lang_model.LM_Queries.query;
import static org.junit.jupiter.api.Assertions.*;

abstract class TypeTest<TYPE extends C_Type>
{
   private final Function<AP_Context, TYPE> typeSupplier;

   protected TypeTest(Function<AP_Context, TYPE> typeSupplier) {this.typeSupplier = typeSupplier;}

   @Test
   void testRepresentsSameType()
   {
      ProcessorTest.process(context ->
                            {
                               assertTrue(query(getTypeSupplier().apply(context))
                                                .representsSameType(getTypeSupplier().apply(context)));
                               assertFalse(query(getTypeSupplier().apply(context))
                                                 .representsSameType(context.getClassOrThrow("java.util.jar.Attributes")));
                               assertFalse(query(getTypeSupplier().apply(context))
                                                 .representsSameType(context.getConstants().getUnboundWildcard()));
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
      ProcessorTest.process(context ->
                            {
                               assertEquals(getTypeSupplier().apply(context), getTypeSupplier().apply(context));
                               assertNotEquals(getTypeSupplier().apply(context), context.getClassOrThrow("java.util.jar.Attributes"));
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
      ProcessorTest.process(context ->
                            {
                               assertEquals(context.getInterfaceOrThrow("java.util.Collection"),
                                            context.withGenerics(context.getInterfaceOrThrow("java.util.Collection"),
                                                                 "java.lang.Object").erasure());

                               assertEquals(context.getInterfaceOrThrow("java.util.Collection"),
                                            context.withGenerics(context.getInterfaceOrThrow("java.util.Collection"), "java.lang.Object"));
                            })
                   .compile();
   }

   protected Function<AP_Context, TYPE> getTypeSupplier()
   {
      return typeSupplier;
   }
}
