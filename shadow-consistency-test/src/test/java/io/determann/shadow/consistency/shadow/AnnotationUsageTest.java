package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.LangModelQueries;
import io.determann.shadow.api.lang_model.shadow.AnnotationUsageLangModel;
import io.determann.shadow.api.lang_model.shadow.AnnotationValueLangModel;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.AnnotationValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static org.junit.jupiter.api.Assertions.*;

class AnnotationUsageTest
{
   @Test
   void testGetValues()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               AnnotationUsageLangModel defaultValues = query(query(shadowApi.getClassOrThrow("AnnotationUsageExample"))
                                                                                       .getAnnotationUsages()
                                                                                       .get(0));

                               assertEquals("string Value", query(defaultValues.getValueOrThrow("stingValue")).getValue());
                               assertEquals(false, query(defaultValues.getValueOrThrow("booleanValue")).getValue());
                               assertEquals((byte) 1, query(defaultValues.getValueOrThrow("byteValue")).getValue());
                               assertEquals((short) 2, query(defaultValues.getValueOrThrow("shortValue")).getValue());
                               assertEquals(3, query(defaultValues.getValueOrThrow("intValue")).getValue());
                               assertEquals(4L, query(defaultValues.getValueOrThrow("longValue")).getValue());
                               assertEquals('a', query(defaultValues.getValueOrThrow("charValue")).getValue());
                               assertEquals(5f, query(defaultValues.getValueOrThrow("floatValue")).getValue());
                               assertEquals(6D, query(defaultValues.getValueOrThrow("doubleValue")).getValue());
                               assertEquals(shadowApi.getClassOrThrow("java.lang.String"), query(defaultValues.getValueOrThrow("typeValue")).getValue());
                               assertEquals(query(shadowApi.getEnumOrThrow("java.lang.annotation.ElementType"))
                                                     .getEnumConstantOrThrow("ANNOTATION_TYPE"),
                                            query(defaultValues.getValueOrThrow("enumConstantValue")).getValue());
                               assertEquals(query(shadowApi.getEnumOrThrow("java.lang.annotation.RetentionPolicy")).getEnumConstantOrThrow(
                                                  "CLASS"),
                                            ((AnnotationValueLangModel) query(((AnnotationValueLangModel.AnnotationUsageValue) defaultValues.getValueOrThrow(
                                                  "annotationUsageValue")).getValue()).getValueOrThrow("value")).getValue());

                               AnnotationValue asListOfValues = defaultValues.getValueOrThrow("asListOfValues");
                               AnnotationValueLangModel valueLangModel = query(asListOfValues);
                               List<AnnotationValueLangModel> values = ((AnnotationValueLangModel.Values) valueLangModel).getValue();
                               List<Object> list = values.stream().map(AnnotationValueLangModel::getValue).toList();
                               assertEquals(List.of('b', 'c'), list);

                               assertTrue(defaultValues.getValues().values().stream().map(LangModelQueries::query).allMatch(AnnotationValueLangModel::isDefault));

                               AnnotationUsage overwrittenStringValue = query(query(shadowApi.getClassOrThrow("AnnotationUsageExample"))
                                     .getFieldOrThrow("testField"))
                                     .getAnnotationUsages()
                                     .get(0);

                               AnnotationValue annotationValue = query(overwrittenStringValue).getValueOrThrow("stingValue");
                               assertFalse(query(annotationValue).isDefault());
                               assertEquals("custom Value", query(annotationValue).getValue());
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
      ProcessorTest.process(shadowApi ->
                            {
                               AnnotationUsageLangModel defaultValues = query(query(shadowApi.getClassOrThrow("AnnotationUsageExample"))
                                                                        .getAnnotationUsages()
                                                                        .get(0));

                               Function<AnnotationValueLangModel, Integer> mapper = value ->
                                     switch (value)
                                     {
                                        case AnnotationValueLangModel.AnnotationUsageValue annotationUsageValue -> 11;
                                        case AnnotationValueLangModel.BooleanValue booleanValue -> 1;
                                        case AnnotationValueLangModel.ByteValue byteValue -> 2;
                                        case AnnotationValueLangModel.CharacterValue characterValue -> 6;
                                        case AnnotationValueLangModel.DoubleValue doubleValue -> 8;
                                        case AnnotationValueLangModel.EnumConstantValue enumConstantValue -> 10;
                                        case AnnotationValueLangModel.FloatValue floatValue -> 7;
                                        case AnnotationValueLangModel.IntegerValue integerValue -> 4;
                                        case AnnotationValueLangModel.LongValue longValue -> 5;
                                        case AnnotationValueLangModel.ShortValue shortValue -> 3;
                                        case AnnotationValueLangModel.StringValue stringValue -> 0;
                                        case AnnotationValueLangModel.TypeValue typeValue -> 9;
                                        case AnnotationValueLangModel.Values values -> 12;
                                     };

                               assertEquals(0, mapper.apply(query(defaultValues.getValueOrThrow("stingValue"))));
                               assertEquals(1, mapper.apply(query(defaultValues.getValueOrThrow("booleanValue"))));
                               assertEquals(2, mapper.apply(query(defaultValues.getValueOrThrow("byteValue"))));
                               assertEquals(3, mapper.apply(query(defaultValues.getValueOrThrow("shortValue"))));
                               assertEquals(4, mapper.apply(query(defaultValues.getValueOrThrow("intValue"))));
                               assertEquals(5, mapper.apply(query(defaultValues.getValueOrThrow("longValue"))));
                               assertEquals(6, mapper.apply(query(defaultValues.getValueOrThrow("charValue"))));
                               assertEquals(7, mapper.apply(query(defaultValues.getValueOrThrow("floatValue"))));
                               assertEquals(8, mapper.apply(query(defaultValues.getValueOrThrow("doubleValue"))));
                               assertEquals(9, mapper.apply(query(defaultValues.getValueOrThrow("typeValue"))));
                               assertEquals(10, mapper.apply(query(defaultValues.getValueOrThrow("enumConstantValue"))));
                               assertEquals(11, mapper.apply(query(defaultValues.getValueOrThrow("annotationUsageValue"))));
                               assertEquals(12, mapper.apply(query(defaultValues.getValueOrThrow("asListOfValues"))));
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
