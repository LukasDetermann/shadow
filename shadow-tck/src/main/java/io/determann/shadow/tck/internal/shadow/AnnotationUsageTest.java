package io.determann.shadow.tck.internal.shadow;

import io.determann.shadow.api.C;
import io.determann.shadow.tck.internal.TckTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.*;

class AnnotationUsageTest
{
   private TckTest.Builder withSimpleUsage()
   {
      return withSource("AnnotationUsageExample.java", """
            @AnnotationUsageAnnotation
            public class AnnotationUsageExample {}
            """);
   }

   private Object get(C.AnnotationUsage annotationUsage, String valueName)
   {
      C.AnnotationValue annotationValue = requestOrThrow(annotationUsage, ANNOTATION_USAGE_GET_VALUE, valueName);
      return requestOrThrow(annotationValue, ANNOTATION_VALUE_GET_VALUE);
   }

   @Test
   void stingValue()
   {
      withSimpleUsage().withSource("AnnotationUsageAnnotation.java", """
            @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
            public @interface AnnotationUsageAnnotation {
               String stingValue() default "string Value";
            }
            """).test(implementation ->
                      {
                         C.Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C.AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

                         assertEquals("string Value", get(defaultValues, "stingValue"));
                      });
   }

   @Test
   void booleanValue()
   {
      withSimpleUsage().withSource("AnnotationUsageAnnotation.java", """
            @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
            public @interface AnnotationUsageAnnotation {
               boolean booleanValue() default false;
            }
            """).test(implementation ->
                      {
                         C.Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C.AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

                         assertEquals(false, get(defaultValues, "booleanValue"));
                      });
   }

   @Test
   void byteValue()
   {
      withSimpleUsage().withSource("AnnotationUsageAnnotation.java", """
            @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
            public @interface AnnotationUsageAnnotation {
               byte byteValue() default 1;;
            }
            """).test(implementation ->
                      {
                         C.Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C.AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

                         assertEquals((byte)1, get(defaultValues, "byteValue"));
                      });
   }

   @Test
   void shortValue()
   {
      withSimpleUsage().withSource("AnnotationUsageAnnotation.java", """
            @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
            public @interface AnnotationUsageAnnotation {
               short shortValue() default 2;
            }
            """).test(implementation ->
                      {
                         C.Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C.AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

                         assertEquals((short) 2, get(defaultValues, "shortValue"));
                      });
   }

   @Test
   void intValue()
   {
      withSimpleUsage().withSource("AnnotationUsageAnnotation.java", """
            @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
            public @interface AnnotationUsageAnnotation {
               int intValue() default 3;
            }
            """).test(implementation ->
                      {
                         C.Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C.AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

                         assertEquals(3, get(defaultValues, "intValue"));
                      });
   }

   @Test
   void longValue()
   {
      withSimpleUsage().withSource("AnnotationUsageAnnotation.java", """
            @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
            public @interface AnnotationUsageAnnotation {
               long longValue() default 4L;
            }
            """).test(implementation ->
                      {
                         C.Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C.AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

                         assertEquals(4L, get(defaultValues, "longValue"));
                      });
   }

   @Test
   void charValue()
   {
      withSimpleUsage().withSource("AnnotationUsageAnnotation.java", """
            @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
            public @interface AnnotationUsageAnnotation {
               char charValue() default 'a';
            }
            """).test(implementation ->
                      {
                         C.Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C.AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

                         assertEquals('a', get(defaultValues, "charValue"));
                      });
   }

   @Test
   void floatValue()
   {
      withSimpleUsage().withSource("AnnotationUsageAnnotation.java", """
            @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
            public @interface AnnotationUsageAnnotation {
               float floatValue() default 5f;
            }
            """).test(implementation ->
                      {
                         C.Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C.AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

                         assertEquals(5f, get(defaultValues, "floatValue"));
                      });
   }

   @Test
   void doubleValue()
   {
      withSimpleUsage().withSource("AnnotationUsageAnnotation.java", """
            @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
            public @interface AnnotationUsageAnnotation {
               double doubleValue() default 6D;
            }
            """).test(implementation ->
                      {
                         C.Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C.AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

                         assertEquals(6D, get(defaultValues, "doubleValue"));
                      });
   }

   @Test
   void typeValue()
   {
      withSimpleUsage().withSource("AnnotationUsageAnnotation.java", """
            @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
            public @interface AnnotationUsageAnnotation {
               Class<String> typeValue() default String.class;
            }
            """).test(implementation ->
                      {
                         C.Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C.AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

                         assertEquals(requestOrThrow(implementation, GET_CLASS, "java.lang.String"), get(defaultValues, "typeValue"));
                      });
   }

   @Test
   void enumConstantValue()
   {
      withSimpleUsage().withSource("AnnotationUsageAnnotation.java", """
            import java.lang.annotation.ElementType;
            
            @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
            public @interface AnnotationUsageAnnotation {
               ElementType enumConstantValue() default ElementType.ANNOTATION_TYPE;
            }
            """).test(implementation ->
                      {
                         C.Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C.AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

                         C.Enum elementType = requestOrThrow(implementation, GET_ENUM, "java.lang.annotation.ElementType");
                         C.EnumConstant annotationType = requestOrThrow(elementType, ENUM_GET_ENUM_CONSTANT, "ANNOTATION_TYPE");
                         assertEquals(annotationType, get(defaultValues, "enumConstantValue"));
                      });
   }

   @Test
   void annotationUsageValue()
   {
      withSimpleUsage().withSource("AnnotationUsageAnnotation.java", """
            import java.lang.annotation.Retention;
            import java.lang.annotation.RetentionPolicy;
            
            @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
            public @interface AnnotationUsageAnnotation {
               Retention annotationUsageValue() default @Retention(RetentionPolicy.CLASS);
            }
            """).test(implementation ->
                      {
                         C.Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C.AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

                         C.Enum retentionPolicy = requestOrThrow(implementation, GET_ENUM, "java.lang.annotation.RetentionPolicy");
                         C.EnumConstant classPolicy = requestOrThrow(retentionPolicy, ENUM_GET_ENUM_CONSTANT, "CLASS");

                         C.AnnotationUsage defaultUsage = (C.AnnotationUsage) get(defaultValues, "annotationUsageValue");
                         C.AnnotationValue defaultRetentionValue = requestOrThrow(defaultUsage, ANNOTATION_USAGE_GET_VALUE, "value");
                         assertEquals(classPolicy, requestOrThrow(defaultRetentionValue, ANNOTATION_VALUE_GET_VALUE));
                      });
   }

   @Test
   void listOfValues()
   {
      withSimpleUsage().withSource("AnnotationUsageAnnotation.java", """
            @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
            public @interface AnnotationUsageAnnotation {
               char[] asListOfValues() default {'b', 'c'};
            }
            """).test(implementation ->
                      {
                         C.Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C.AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

                         //noinspection unchecked
                         List<C.AnnotationValue> values = (List<C.AnnotationValue>) get(defaultValues, "asListOfValues");
                         List<Object> list = values.stream().map(value -> requestOrThrow(value, ANNOTATION_VALUE_GET_VALUE)).toList();
                         assertEquals(List.of('b', 'c'), list);
                      });
   }

   @Test
   void isDefault()
   {
      withSource("AnnotationUsageAnnotation.java", """
            import java.lang.annotation.ElementType;
            import java.lang.annotation.Retention;
            import java.lang.annotation.RetentionPolicy;
            
            @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
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
            """).withSource("AnnotationUsageExample.java", """
            @AnnotationUsageAnnotation
            public class AnnotationUsageExample {}
            """).test(implementation ->
                      {
                         C.Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C.AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

                         assertTrue(requestOrThrow(defaultValues, ANNOTATION_USAGE_GET_VALUES).values()
                                                                                              .stream()
                                                                                              .allMatch(cAnnotationValue -> requestOrThrow(
                                                                                                    cAnnotationValue,
                                                                                                    ANNOTATION_VALUE_IS_DEFAULT)));
                      });
   }

   @Test
   void overwriteValue()
   {
      withSource("AnnotationUsageAnnotation.java", """
            @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
            public @interface AnnotationUsageAnnotation {
               String stingValue() default "string Value";
            }
            """).withSource("AnnotationUsageExample.java", """
            public class AnnotationUsageExample {
               @AnnotationUsageAnnotation(stingValue = "custom Value")
               private String testField;
            }
            """).test(implementation ->
                      {
                         C.Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C.Field field = requestOrThrow(cClass, DECLARED_GET_FIELD, "testField");
                         C.AnnotationUsage overwrittenStringValue = requestOrThrow(field, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);
                         C.AnnotationValue annotationValue = requestOrThrow(overwrittenStringValue, ANNOTATION_USAGE_GET_VALUE, "stingValue");
                         assertFalse(requestOrThrow(annotationValue, ANNOTATION_VALUE_IS_DEFAULT));
                         assertEquals("custom Value", requestOrThrow(annotationValue, ANNOTATION_VALUE_GET_VALUE));
                      });
   }
}