package io.determann.shadow.api.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.annotationvalue.AnnotationValueTypeChooser;
import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
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
                                                           .map(AnnotationValueTypeChooser::asCharacter)
                                                           .collect(Collectors.toList()));

                                 assertTrue(defaultValues.getValues().values().stream().allMatch(AnnotationValueTypeChooser::isDefaultValue));

                                 AnnotationUsage overwrittenStringValue = shadowApi.getClassOrThrow("AnnotationUsageExample")
                                                                                   .getFieldOrThrow("testField")
                                                                                   .getAnnotationUsages()
                                                                                   .get(0);

                                 AnnotationValueTypeChooser overwrittenValueTypeChooser = overwrittenStringValue.getValueOrThrow("stingValue");
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
}
