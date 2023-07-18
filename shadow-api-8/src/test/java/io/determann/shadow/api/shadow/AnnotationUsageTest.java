package io.determann.shadow.api.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.annotationvalue.AnnotationValue;
import io.determann.shadow.api.annotationvalue.AnnotationValueConsumer;
import io.determann.shadow.api.annotationvalue.AnnotationValueMapper;
import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationUsageTest extends AnnotationTest<AnnotationUsage>
{
   @Override
   protected Function<ShadowApi, AnnotationUsage> getShadowSupplier()
   {
      //junit doesn't like multiple constructors. Therefore, AnnotationTest can only supply its supplier to its parent
      return shadowApi -> shadowApi.getClassOrThrow("AnnotationUsageExample")
                                   .getAnnotationUsages()
                                   .get(0);
   }

   @Test
   void testGetValues()
   {
      ProcessorTest.process(shadowApi ->
                              {
                                 AnnotationUsage defaultValues = getShadowSupplier().apply(shadowApi);

                                 assertEquals("string Value", defaultValues.getValueOrThrow("stingValue").asString());
                                 assertEquals(false, defaultValues.getValueOrThrow("booleanValue").asBoolean());
                                 assertEquals((byte) 1, defaultValues.getValueOrThrow("byteValue").asByte());
                                 assertEquals((short) 2, defaultValues.getValueOrThrow("shortValue").asShort());
                                 assertEquals(3, defaultValues.getValueOrThrow("intValue").asInteger());
                                 assertEquals(4L, defaultValues.getValueOrThrow("longValue").asLong());
                                 assertEquals('a', defaultValues.getValueOrThrow("charValue").asCharacter());
                                 assertEquals(5f, defaultValues.getValueOrThrow("floatValue").asFloat());
                                 assertEquals(6D, defaultValues.getValueOrThrow("doubleValue").asDouble());
                                 assertEquals(shadowApi.getClassOrThrow("java.lang.String"), defaultValues.getValueOrThrow("typeValue").asType());
                                 assertEquals(shadowApi.getEnumOrThrow("java.lang.annotation.ElementType").getEnumConstantOrThrow("ANNOTATION_TYPE"),
                                              defaultValues.getValueOrThrow("enumConstantValue").asEnumConstant());
                                 assertEquals(shadowApi.getEnumOrThrow("java.lang.annotation.RetentionPolicy").getEnumConstantOrThrow("CLASS"),
                                              defaultValues.getValueOrThrow("annotationUsageValue")
                                                           .asAnnotationUsage()
                                                           .getValueOrThrow("value")
                                                           .asEnumConstant());
                                 assertEquals(Arrays.asList('b', 'c'),
                                              defaultValues.getValueOrThrow("asListOfValues")
                                                           .asListOfValues()
                                                           .stream()
                                                           .map(AnnotationValue::asCharacter)
                                                           .collect(Collectors.toList()));

                                 assertTrue(defaultValues.getValues().values().stream().allMatch(AnnotationValue::isDefaultValue));

                                 AnnotationUsage overwrittenStringValue = shadowApi.getClassOrThrow("AnnotationUsageExample")
                                                                                   .getFieldOrThrow("testField")
                                                                                   .getAnnotationUsages()
                                                                                   .get(0);

                                 AnnotationValue overwrittenValueTypeChooser = overwrittenStringValue.getValueOrThrow("stingValue");
                                 assertFalse(overwrittenValueTypeChooser.isDefaultValue());
                                 assertEquals("custom Value", overwrittenValueTypeChooser.asString());
                              })
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
   void testMapValues()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               AnnotationUsage defaultValues = getShadowSupplier().apply(shadowApi);

                               AnnotationValueMapper<Integer> mapper = new AnnotationValueMapper<Integer>()
                               {
                                  @Override
                                  public Integer string(String value)
                                  {
                                     return 0;
                                  }

                                  @Override
                                  public Integer aBoolean(Boolean value)
                                  {
                                     return 1;
                                  }

                                  @Override
                                  public Integer aByte(Byte value)
                                  {
                                     return 2;
                                  }

                                  @Override
                                  public Integer aShort(Short value)
                                  {
                                     return 3;
                                  }

                                  @Override
                                  public Integer integer(Integer value)
                                  {
                                     return 4;
                                  }

                                  @Override
                                  public Integer aLong(Long value)
                                  {
                                     return 5;
                                  }

                                  @Override
                                  public Integer character(Character value)
                                  {
                                     return 6;
                                  }

                                  @Override
                                  public Integer aFloat(Float value)
                                  {
                                     return 7;
                                  }

                                  @Override
                                  public Integer aDouble(Double value)
                                  {
                                     return 8;
                                  }

                                  @Override
                                  public Integer type(Shadow<TypeMirror> value)
                                  {
                                     return 9;
                                  }

                                  @Override
                                  public Integer enumConstant(EnumConstant value)
                                  {
                                     return 10;
                                  }

                                  @Override
                                  public Integer annotationUsage(AnnotationUsage value)
                                  {
                                     return 11;
                                  }

                                  @Override
                                  public Integer values(List<AnnotationValue> values)
                                  {
                                     return 12;
                                  }
                               };

                               assertEquals(0, defaultValues.getValueOrThrow("stingValue").map(mapper));
                               assertEquals(1, defaultValues.getValueOrThrow("booleanValue").map(mapper));
                               assertEquals(2, defaultValues.getValueOrThrow("byteValue").map(mapper));
                               assertEquals(3, defaultValues.getValueOrThrow("shortValue").map(mapper));
                               assertEquals(4, defaultValues.getValueOrThrow("intValue").map(mapper));
                               assertEquals(5, defaultValues.getValueOrThrow("longValue").map(mapper));
                               assertEquals(6, defaultValues.getValueOrThrow("charValue").map(mapper));
                               assertEquals(7, defaultValues.getValueOrThrow("floatValue").map(mapper));
                               assertEquals(8, defaultValues.getValueOrThrow("doubleValue").map(mapper));
                               assertEquals(9, defaultValues.getValueOrThrow("typeValue").map(mapper));
                               assertEquals(10, defaultValues.getValueOrThrow("enumConstantValue").map(mapper));
                               assertEquals(11, defaultValues.getValueOrThrow("annotationUsageValue").map(mapper));
                               assertEquals(12, defaultValues.getValueOrThrow("asListOfValues").map(mapper));
                            })
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
   void testConsumeValues()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               AnnotationUsage defaultValues = getShadowSupplier().apply(shadowApi);

                               AtomicInteger counter = new AtomicInteger(0);

                               AnnotationValueConsumer consumer = new AnnotationValueConsumer()
                               {
                                  @Override
                                  public void string(String value)
                                  {
                                     assertEquals("string Value", value);
                                     counter.incrementAndGet();
                                  }

                                  @Override
                                  public void aBoolean(Boolean value)
                                  {
                                     assertFalse(value);
                                     counter.incrementAndGet();
                                  }

                                  @Override
                                  public void aByte(Byte value)
                                  {
                                     assertEquals((byte) 1, value);
                                     counter.incrementAndGet();
                                  }

                                  @Override
                                  public void aShort(Short value)
                                  {
                                     assertEquals((short) 2, value);
                                     counter.incrementAndGet();
                                  }

                                  @Override
                                  public void integer(Integer value)
                                  {
                                     assertEquals(3, value);
                                     counter.incrementAndGet();
                                  }

                                  @Override
                                  public void aLong(Long value)
                                  {
                                     assertEquals(4, value);
                                     counter.incrementAndGet();
                                  }

                                  @Override
                                  public void character(Character value)
                                  {
                                     assertEquals('a', value);
                                     counter.incrementAndGet();
                                  }

                                  @Override
                                  public void aFloat(Float value)
                                  {
                                     assertEquals(5, value);
                                     counter.incrementAndGet();
                                  }

                                  @Override
                                  public void aDouble(Double value)
                                  {
                                     assertEquals(6, value);
                                     counter.incrementAndGet();
                                  }

                                  @Override
                                  public void type(Shadow<TypeMirror> value)
                                  {
                                     assertEquals(shadowApi.getClassOrThrow("java.lang.String"), value);
                                     counter.incrementAndGet();
                                  }

                                  @Override
                                  public void enumConstant(EnumConstant value)
                                  {
                                     assertEquals(shadowApi.getEnumOrThrow("java.lang.annotation.ElementType")
                                                           .getEnumConstantOrThrow("ANNOTATION_TYPE"),
                                                  value);
                                     counter.incrementAndGet();
                                  }

                                  @Override
                                  public void annotationUsage(AnnotationUsage value)
                                  {
                                     assertEquals(shadowApi.getEnumOrThrow("java.lang.annotation.RetentionPolicy")
                                                           .getEnumConstantOrThrow("CLASS"),
                                                  value.getValueOrThrow("value").asEnumConstant());
                                     counter.incrementAndGet();
                                  }

                                  @Override
                                  public void values(List<AnnotationValue> values)
                                  {
                                     assertEquals(Arrays.asList('b', 'c'),
                                                  values.stream().map(AnnotationValue::asCharacter).collect(Collectors.toList()));
                                     counter.incrementAndGet();
                                  }
                               };

                               defaultValues.getValueOrThrow("stingValue").consume(consumer);
                               defaultValues.getValueOrThrow("booleanValue").consume(consumer);
                               defaultValues.getValueOrThrow("byteValue").consume(consumer);
                               defaultValues.getValueOrThrow("shortValue").consume(consumer);
                               defaultValues.getValueOrThrow("intValue").consume(consumer);
                               defaultValues.getValueOrThrow("longValue").consume(consumer);
                               defaultValues.getValueOrThrow("charValue").consume(consumer);
                               defaultValues.getValueOrThrow("floatValue").consume(consumer);
                               defaultValues.getValueOrThrow("doubleValue").consume(consumer);
                               defaultValues.getValueOrThrow("typeValue").consume(consumer);
                               defaultValues.getValueOrThrow("enumConstantValue").consume(consumer);
                               defaultValues.getValueOrThrow("annotationUsageValue").consume(consumer);
                               defaultValues.getValueOrThrow("asListOfValues").consume(consumer);

                               assertEquals(13, counter.get());
                            })
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
}
