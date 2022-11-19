package org.determann.shadow.api.shadow;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.test.CompilationTest;
import org.determann.shadow.api.wrapper.AnnotationValueTypeChooser;
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
                                 assertEquals(shadowApi.getClassOrThrow("java.lang.String"), defaultValues.getValue("typeValue").asType());
                                 assertEquals(shadowApi.getEnumOrThrow("java.lang.annotation.ElementType").getEnumConstantOrThrow("ANNOTATION_TYPE"),
                                              defaultValues.getValue("enumConstantValue").asEnumConstant());
                                 assertEquals(shadowApi.getEnumOrThrow("java.lang.annotation.RetentionPolicy").getEnumConstantOrThrow("CLASS"),
                                              defaultValues.getValue("annotationUsageValue")
                                                           .asAnnotationUsage()
                                                           .getValue("value")
                                                           .asEnumConstant());
                                 assertEquals(List.of('b', 'c'),
                                              defaultValues.getValue("asListOfValues")
                                                           .asListOfValues()
                                                           .stream()
                                                           .map(AnnotationValueTypeChooser::asCharacter)
                                                           .toList());

                                 assertTrue(defaultValues.getValues().values().stream().allMatch(AnnotationValueTypeChooser::isDefaultValue));

                                 AnnotationUsage overwrittenStringValue = shadowApi.getClassOrThrow("AnnotationUsageExample")
                                                                                   .getFieldOrThrow("testField")
                                                                                   .getAnnotationUsages()
                                                                                   .get(0);

                                 AnnotationValueTypeChooser overwrittenValueTypeChooser = overwrittenStringValue.getValue("stingValue");
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
