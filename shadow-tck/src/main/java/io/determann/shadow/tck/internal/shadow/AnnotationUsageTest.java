package io.determann.shadow.tck.internal.shadow;

import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.C_AnnotationValue;
import io.determann.shadow.api.shadow.structure.C_EnumConstant;
import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Enum;
import io.determann.shadow.tck.internal.TckTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
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

   private Object get(C_AnnotationUsage annotationUsage, String valueName)
   {
      C_AnnotationValue annotationValue = requestOrThrow(annotationUsage, ANNOTATION_USAGE_GET_VALUE, valueName);
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
                         C_Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C_AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

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
                         C_Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C_AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

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
                         C_Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C_AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

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
                         C_Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C_AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

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
                         C_Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C_AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

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
                         C_Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C_AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

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
                         C_Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C_AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

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
                         C_Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C_AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

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
                         C_Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C_AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

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
                         C_Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C_AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

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
                         C_Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C_AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

                         C_Enum elementType = requestOrThrow(implementation, GET_ENUM, "java.lang.annotation.ElementType");
                         C_EnumConstant annotationType = requestOrThrow(elementType, ENUM_GET_ENUM_CONSTANT, "ANNOTATION_TYPE");
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
                         C_Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C_AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

                         C_Enum retentionPolicy = requestOrThrow(implementation, GET_ENUM, "java.lang.annotation.RetentionPolicy");
                         C_EnumConstant classPolicy = requestOrThrow(retentionPolicy, ENUM_GET_ENUM_CONSTANT, "CLASS");

                         C_AnnotationUsage defaultUsage = (C_AnnotationUsage) get(defaultValues, "annotationUsageValue");
                         C_AnnotationValue defaultRetentionValue = requestOrThrow(defaultUsage, ANNOTATION_USAGE_GET_VALUE, "value");
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
                         C_Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C_AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

                         //noinspection unchecked
                         List<C_AnnotationValue> values = (List<C_AnnotationValue>) get(defaultValues, "asListOfValues");
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
                         C_Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C_AnnotationUsage defaultValues = requestOrThrow(cClass, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);

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
                         C_Class cClass = requestOrThrow(implementation, GET_CLASS, "AnnotationUsageExample");
                         C_Field field = requestOrThrow(cClass, DECLARED_GET_FIELD, "testField");
                         C_AnnotationUsage overwrittenStringValue = requestOrThrow(field, ANNOTATIONABLE_GET_ANNOTATION_USAGES).get(0);
                         C_AnnotationValue annotationValue = requestOrThrow(overwrittenStringValue, ANNOTATION_USAGE_GET_VALUE, "stingValue");
                         assertFalse(requestOrThrow(annotationValue, ANNOTATION_VALUE_IS_DEFAULT));
                         assertEquals("custom Value", requestOrThrow(annotationValue, ANNOTATION_VALUE_GET_VALUE));
                      });
   }
}