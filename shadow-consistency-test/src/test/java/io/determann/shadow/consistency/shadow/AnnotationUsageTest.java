package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.LM_Queries;
import io.determann.shadow.api.lang_model.shadow.LM_AnnotationUsage;
import io.determann.shadow.api.lang_model.shadow.LM_AnnotationValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationUsageTest
{
   @Test
   void testGetValues()
   {
      ProcessorTest.process(context ->
                            {
                               LM_AnnotationUsage defaultValues = context.getClassOrThrow("AnnotationUsageExample")
                                                                         .getAnnotationUsages()
                                                                         .get(0);

                               assertEquals("string Value", defaultValues.getValueOrThrow("stingValue").getValue());
                               assertEquals(false, defaultValues.getValueOrThrow("booleanValue").getValue());
                               assertEquals((byte) 1, defaultValues.getValueOrThrow("byteValue").getValue());
                               assertEquals((short) 2, defaultValues.getValueOrThrow("shortValue").getValue());
                               assertEquals(3, defaultValues.getValueOrThrow("intValue").getValue());
                               assertEquals(4L, defaultValues.getValueOrThrow("longValue").getValue());
                               assertEquals('a', defaultValues.getValueOrThrow("charValue").getValue());
                               assertEquals(5f, defaultValues.getValueOrThrow("floatValue").getValue());
                               assertEquals(6D, defaultValues.getValueOrThrow("doubleValue").getValue());
                               assertEquals(context.getClassOrThrow("java.lang.String"), defaultValues.getValueOrThrow("typeValue").getValue());
                               assertEquals(context.getEnumOrThrow("java.lang.annotation.ElementType")
                                                     .getEnumConstantOrThrow("ANNOTATION_TYPE"),
                                            defaultValues.getValueOrThrow("enumConstantValue").getValue());
                               assertEquals(context.getEnumOrThrow("java.lang.annotation.RetentionPolicy").getEnumConstantOrThrow(
                                                  "CLASS"),
                                            ((LM_AnnotationValue.AnnotationUsageValue) defaultValues.getValueOrThrow(
                                                  "annotationUsageValue")).getValue().getValueOrThrow("value").getValue());

                               LM_AnnotationValue asListOfValues = defaultValues.getValueOrThrow("asListOfValues");
                               List<LM_AnnotationValue> values = ((LM_AnnotationValue.Values) asListOfValues).getValue();
                               List<Object> list = values.stream().map(LM_AnnotationValue::getValue).toList();
                               assertEquals(List.of('b', 'c'), list);

                               assertTrue(defaultValues.getValues().values().stream().map(LM_Queries::query).allMatch(LM_AnnotationValue::isDefault));

                               LM_AnnotationUsage overwrittenStringValue = context.getClassOrThrow("AnnotationUsageExample")
                                                                                  .getFieldOrThrow("testField")
                                                                                  .getAnnotationUsages()
                                                                                  .get(0);

                               LM_AnnotationValue annotationValue = overwrittenStringValue.getValueOrThrow("stingValue");
                               assertFalse(annotationValue.isDefault());
                               assertEquals("custom Value", annotationValue.getValue());
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

   @Test
   void testMapValues()
   {
      ProcessorTest.process(context ->
                            {
                               LM_AnnotationUsage defaultValues = context.getClassOrThrow("AnnotationUsageExample")
                                                                         .getAnnotationUsages()
                                                                         .get(0);

                               Function<LM_AnnotationValue, Integer> mapper = value ->
                                     switch (value)
                                     {
                                        case LM_AnnotationValue.AnnotationUsageValue annotationUsageValue -> 11;
                                        case LM_AnnotationValue.BooleanValue booleanValue -> 1;
                                        case LM_AnnotationValue.ByteValue byteValue -> 2;
                                        case LM_AnnotationValue.CharacterValue characterValue -> 6;
                                        case LM_AnnotationValue.DoubleValue doubleValue -> 8;
                                        case LM_AnnotationValue.EnumConstantValue enumConstantValue -> 10;
                                        case LM_AnnotationValue.FloatValue floatValue -> 7;
                                        case LM_AnnotationValue.IntegerValue integerValue -> 4;
                                        case LM_AnnotationValue.LongValue longValue -> 5;
                                        case LM_AnnotationValue.ShortValue shortValue -> 3;
                                        case LM_AnnotationValue.StringValue stringValue -> 0;
                                        case LM_AnnotationValue.TypeValue typeValue -> 9;
                                        case LM_AnnotationValue.Values values -> 12;
                                     };

                               assertEquals(0, mapper.apply(defaultValues.getValueOrThrow("stingValue")));
                               assertEquals(1, mapper.apply(defaultValues.getValueOrThrow("booleanValue")));
                               assertEquals(2, mapper.apply(defaultValues.getValueOrThrow("byteValue")));
                               assertEquals(3, mapper.apply(defaultValues.getValueOrThrow("shortValue")));
                               assertEquals(4, mapper.apply(defaultValues.getValueOrThrow("intValue")));
                               assertEquals(5, mapper.apply(defaultValues.getValueOrThrow("longValue")));
                               assertEquals(6, mapper.apply(defaultValues.getValueOrThrow("charValue")));
                               assertEquals(7, mapper.apply(defaultValues.getValueOrThrow("floatValue")));
                               assertEquals(8, mapper.apply(defaultValues.getValueOrThrow("doubleValue")));
                               assertEquals(9, mapper.apply(defaultValues.getValueOrThrow("typeValue")));
                               assertEquals(10, mapper.apply(defaultValues.getValueOrThrow("enumConstantValue")));
                               assertEquals(11, mapper.apply(defaultValues.getValueOrThrow("annotationUsageValue")));
                               assertEquals(12, mapper.apply(defaultValues.getValueOrThrow("asListOfValues")));
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
