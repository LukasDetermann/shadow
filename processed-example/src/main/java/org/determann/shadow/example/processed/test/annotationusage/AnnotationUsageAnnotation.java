package org.determann.shadow.example.processed.test.annotationusage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public @interface AnnotationUsageAnnotation
{
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
