package io.determann.shadow.api.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.test.ProcessorTest;
import io.determann.shadow.api.wrapper.AnnotationValueTypeChooser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

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
                                 assertEquals(List.of('b', 'c'),
                                              defaultValues.getValueOrThrow("asListOfValues")
                                                           .asListOfValues()
                                                           .stream()
                                                           .map(AnnotationValueTypeChooser::asCharacter)
                                                           .toList());

                                 assertTrue(defaultValues.getValues().values().stream().allMatch(AnnotationValueTypeChooser::isDefaultValue));

                                 AnnotationUsage overwrittenStringValue = shadowApi.getClassOrThrow("AnnotationUsageExample")
                                                                                   .getFieldOrThrow("testField")
                                                                                   .getAnnotationUsages()
                                                                                   .get(0);

                                 AnnotationValueTypeChooser overwrittenValueTypeChooser = overwrittenStringValue.getValueOrThrow("stingValue");
                                 assertFalse(overwrittenValueTypeChooser.isDefaultValue());
                                 assertEquals("custom Value", overwrittenValueTypeChooser.asString());
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
