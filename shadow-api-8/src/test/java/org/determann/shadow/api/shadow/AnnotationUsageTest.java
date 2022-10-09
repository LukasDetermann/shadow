package org.determann.shadow.api.shadow;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.test.CompilationTest;
import org.determann.shadow.api.wrapper.AnnotationValueTypeChooser;
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
      return shadowApi -> shadowApi.getClass("AnnotationUsageExample")
                                   .getAnnotationUsages()
                                   .get(0);
   }

   @Test
   void testGetValues()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 AnnotationUsage defaultValues = getShadowSupplier().apply(shadowApi);

                                 assertEquals("string Value", defaultValues.getValue("stingValue").asString());
                                 assertEquals(false, defaultValues.getValue("booleanValue").asBoolean());
                                 assertEquals((byte) 1, defaultValues.getValue("byteValue").asByte());
                                 assertEquals((short) 2, defaultValues.getValue("shortValue").asShort());
                                 assertEquals(3, defaultValues.getValue("intValue").asInteger());
                                 assertEquals(4L, defaultValues.getValue("longValue").asLong());
                                 assertEquals('a', defaultValues.getValue("charValue").asCharacter());
                                 assertEquals(5f, defaultValues.getValue("floatValue").asFloat());
                                 assertEquals(6D, defaultValues.getValue("doubleValue").asDouble());
                                 assertEquals(shadowApi.getClass("java.lang.String"), defaultValues.getValue("typeValue").asType());
                                 assertEquals(shadowApi.getEnum("java.lang.annotation.ElementType").getEnumConstant("ANNOTATION_TYPE"),
                                              defaultValues.getValue("enumConstantValue").asEnumConstant());
                                 assertEquals(shadowApi.getEnum("java.lang.annotation.RetentionPolicy").getEnumConstant("CLASS"),
                                              defaultValues.getValue("annotationUsageValue")
                                                           .asAnnotationUsage()
                                                           .getValue("value")
                                                           .asEnumConstant());
                                 assertEquals(Arrays.asList('b', 'c'),
                                              defaultValues.getValue("asListOfValues")
                                                           .asListOfValues()
                                                           .stream()
                                                           .map(AnnotationValueTypeChooser::asCharacter)
                                                           .collect(Collectors.toList()));

                                 assertTrue(defaultValues.getValues().values().stream().allMatch(AnnotationValueTypeChooser::isDefaultValue));

                                 AnnotationUsage overwrittenStringValue = shadowApi.getClass("AnnotationUsageExample")
                                                                                   .getField("testField")
                                                                                   .getAnnotationUsages()
                                                                                   .get(0);

                                 AnnotationValueTypeChooser overwrittenValueTypeChooser = overwrittenStringValue.getValue("stingValue");
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
