package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.AnnotationUsage;
import org.determann.shadow.api.wrapper.AnnotationValueTypeChooser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.*;

public class AnnotationUsageTest extends AnnotationTest<AnnotationUsage>
{
   @Override
   protected Supplier<AnnotationUsage> getShadowSupplier()
   {
      //junit doesn't like multiple constructors. Therefore, AnnotationTest can only supply its supplier to its parent
      return () -> SHADOW_API.annotatedWith("org.determann.shadow.example.processed.test.annotationusage.AnnotationUsageAnnotation")
                             .classes()
                             .iterator()
                             .next()
                             .getAnnotationUsages()
                             .get(0);
   }

   @Test
   void testGetValues()
   {
      AnnotationUsage defaultValues = getShadowSupplier().get();

      assertEquals("string Value", defaultValues.getValue("stingValue").asString());
      assertEquals(false, defaultValues.getValue("booleanValue").asBoolean());
      assertEquals((byte) 1, defaultValues.getValue("byteValue").asByte());
      assertEquals((short) 2, defaultValues.getValue("shortValue").asShort());
      assertEquals(3, defaultValues.getValue("intValue").asInteger());
      assertEquals(4L, defaultValues.getValue("longValue").asLong());
      assertEquals('a', defaultValues.getValue("charValue").asCharacter());
      assertEquals(5f, defaultValues.getValue("floatValue").asFloat());
      assertEquals(6D, defaultValues.getValue("doubleValue").asDouble());
      assertEquals(SHADOW_API.getClass("java.lang.String"), defaultValues.getValue("typeValue").asType());
      assertEquals(SHADOW_API.getEnum("java.lang.annotation.ElementType").getEnumConstant("ANNOTATION_TYPE"),
                   defaultValues.getValue("enumConstantValue").asEnumConstant());
      assertEquals(SHADOW_API.getEnum("java.lang.annotation.RetentionPolicy").getEnumConstant("CLASS"),
                   defaultValues.getValue("annotationUsageValue").asAnnotationUsage().getValue("value").asEnumConstant());
      assertEquals(List.of('b', 'c'),
                   defaultValues.getValue("asListOfValues")
                                .asListOfValues()
                                .stream()
                                .map(AnnotationValueTypeChooser::asCharacter)
                                .toList());

      assertTrue(defaultValues.getValues().values().stream().allMatch(AnnotationValueTypeChooser::isDefaultValue));

      AnnotationUsage overwrittenStringValue = SHADOW_API
            .annotatedWith("org.determann.shadow.example.processed.test.annotationusage.AnnotationUsageAnnotation")
            .fields()
            .iterator()
            .next()
            .getAnnotationUsages()
            .get(0);

      AnnotationValueTypeChooser overwrittenValueTypeChooser = overwrittenStringValue.getValue("stingValue");
      assertFalse(overwrittenValueTypeChooser.isDefaultValue());
      assertEquals("custom Value", overwrittenValueTypeChooser.asString());
   }
}
