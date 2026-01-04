package io.determann.shadow.annotation_processing.shadow;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationUsageTest
{
   @Test
   void stingValue()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("AnnotationUsageExample");
                               Ap.AnnotationUsage defaultValues = cClass.getAnnotationUsages().get(0);

                               assertEquals("string Value", defaultValues.getValueOrThrow("stingValue").getValue());
                            })
                   .withCodeToCompile("AnnotationUsageAnnotation.java", """
                                                                        @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                                                        public @interface AnnotationUsageAnnotation {
                                                                           String stingValue() default "string Value";
                                                                        }
                                                                        """)
                   .withCodeToCompile("AnnotationUsageExample.java", """
                                                                     @AnnotationUsageAnnotation
                                                                     public class AnnotationUsageExample {}
                                                                     """)
                   .compile();
   }

   @Test
   void booleanValue()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("AnnotationUsageExample");
                               Ap.AnnotationUsage defaultValues = cClass.getAnnotationUsages().get(0);

                               assertEquals(false, defaultValues.getValueOrThrow("booleanValue").getValue());
                            })
                   .withCodeToCompile("AnnotationUsageAnnotation.java", """
                                                                        @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                                                        public @interface AnnotationUsageAnnotation {
                                                                           boolean booleanValue() default false;
                                                                        }
                                                                        """)
                   .withCodeToCompile("AnnotationUsageExample.java", """
                                                                     @AnnotationUsageAnnotation
                                                                     public class AnnotationUsageExample {}
                                                                     """)
                   .compile();
   }

   @Test
   void byteValue()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("AnnotationUsageExample");
                               Ap.AnnotationUsage defaultValues = cClass.getAnnotationUsages().get(0);

                               assertEquals((byte) 1, defaultValues.getValueOrThrow("byteValue").getValue());
                            })
                   .withCodeToCompile("AnnotationUsageAnnotation.java", """
                                                                        @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                                                        public @interface AnnotationUsageAnnotation {
                                                                           byte byteValue() default 1;;
                                                                        }
                                                                        """)
                   .withCodeToCompile("AnnotationUsageExample.java", """
                                                                     @AnnotationUsageAnnotation
                                                                     public class AnnotationUsageExample {}
                                                                     """)
                   .compile();
   }

   @Test
   void shortValue()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("AnnotationUsageExample");
                               Ap.AnnotationUsage defaultValues = cClass.getAnnotationUsages().get(0);

                               assertEquals((short) 2, defaultValues.getValueOrThrow("shortValue").getValue());
                            })
                   .withCodeToCompile("AnnotationUsageAnnotation.java", """
                                                                        @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                                                        public @interface AnnotationUsageAnnotation {
                                                                           short shortValue() default 2;
                                                                        }
                                                                        """)
                   .withCodeToCompile("AnnotationUsageExample.java", """
                                                                     @AnnotationUsageAnnotation
                                                                     public class AnnotationUsageExample {}
                                                                     """)
                   .compile();
   }

   @Test
   void intValue()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("AnnotationUsageExample");
                               Ap.AnnotationUsage defaultValues = cClass.getAnnotationUsages().get(0);

                               assertEquals(3, defaultValues.getValueOrThrow("intValue").getValue());
                            })
                   .withCodeToCompile("AnnotationUsageAnnotation.java", """
                                                                        @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                                                        public @interface AnnotationUsageAnnotation {
                                                                           int intValue() default 3;
                                                                        }
                                                                        """)
                   .withCodeToCompile("AnnotationUsageExample.java", """
                                                                     @AnnotationUsageAnnotation
                                                                     public class AnnotationUsageExample {}
                                                                     """)
                   .compile();
   }

   @Test
   void longValue()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("AnnotationUsageExample");
                               Ap.AnnotationUsage defaultValues = cClass.getAnnotationUsages().get(0);

                               assertEquals(4L, defaultValues.getValueOrThrow("longValue").getValue());
                            })
                   .withCodeToCompile("AnnotationUsageAnnotation.java", """
                                                                        @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                                                        public @interface AnnotationUsageAnnotation {
                                                                           long longValue() default 4L;
                                                                        }
                                                                        """)
                   .withCodeToCompile("AnnotationUsageExample.java", """
                                                                     @AnnotationUsageAnnotation
                                                                     public class AnnotationUsageExample {}
                                                                     """)
                   .compile();
   }

   @Test
   void charValue()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("AnnotationUsageExample");
                               Ap.AnnotationUsage defaultValues = cClass.getAnnotationUsages().get(0);

                               assertEquals('a', defaultValues.getValueOrThrow("charValue").getValue());
                            })
                   .withCodeToCompile("AnnotationUsageAnnotation.java", """
                                                                        @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                                                        public @interface AnnotationUsageAnnotation {
                                                                           char charValue() default 'a';
                                                                        }
                                                                        """)
                   .withCodeToCompile("AnnotationUsageExample.java", """
                                                                     @AnnotationUsageAnnotation
                                                                     public class AnnotationUsageExample {}
                                                                     """)
                   .compile();
   }

   @Test
   void floatValue()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("AnnotationUsageExample");
                               Ap.AnnotationUsage defaultValues = cClass.getAnnotationUsages().get(0);

                               assertEquals(5f, defaultValues.getValueOrThrow("floatValue").getValue());
                            })
                   .withCodeToCompile("AnnotationUsageAnnotation.java", """
                                                                        @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                                                        public @interface AnnotationUsageAnnotation {
                                                                           float floatValue() default 5f;
                                                                        }
                                                                        """)
                   .withCodeToCompile("AnnotationUsageExample.java", """
                                                                     @AnnotationUsageAnnotation
                                                                     public class AnnotationUsageExample {}
                                                                     """)
                   .compile();
   }

   @Test
   void doubleValue()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("AnnotationUsageExample");
                               Ap.AnnotationUsage defaultValues = cClass.getAnnotationUsages().get(0);

                               assertEquals(6D, defaultValues.getValueOrThrow("doubleValue").getValue());
                            })
                   .withCodeToCompile("AnnotationUsageAnnotation.java", """
                                                                        @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                                                        public @interface AnnotationUsageAnnotation {
                                                                           double doubleValue() default 6D;
                                                                        }
                                                                        """)
                   .withCodeToCompile("AnnotationUsageExample.java", """
                                                                     @AnnotationUsageAnnotation
                                                                     public class AnnotationUsageExample {}
                                                                     """)
                   .compile();
   }

   @Test
   void typeValue()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("AnnotationUsageExample");
                               Ap.AnnotationUsage defaultValues = cClass.getAnnotationUsages().get(0);

                               assertEquals(context.getClassOrThrow("java.lang.String"), defaultValues.getValueOrThrow("typeValue").getValue());
                            })
                   .withCodeToCompile("AnnotationUsageAnnotation.java", """
                                                                        @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                                                        public @interface AnnotationUsageAnnotation {
                                                                           Class<String> typeValue() default String.class;
                                                                        }
                                                                        """)
                   .withCodeToCompile("AnnotationUsageExample.java", """
                                                                     @AnnotationUsageAnnotation
                                                                     public class AnnotationUsageExample {}
                                                                     """)
                   .compile();
   }

   @Test
   void enumValue()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("AnnotationUsageExample");
                               Ap.AnnotationUsage defaultValues = cClass.getAnnotationUsages().get(0);

                               Ap.Enum elementType = context.getEnumOrThrow("java.lang.annotation.ElementType");
                               Ap.EnumConstant annotationType = elementType.getEnumConstantOrThrow("ANNOTATION_TYPE");
                               assertEquals(annotationType, defaultValues.getValueOrThrow("enumValue").getValue());
                            })
                   .withCodeToCompile("AnnotationUsageAnnotation.java", """
                                                                        import java.lang.annotation.ElementType;
                                                                        
                                                                        @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                                                        public @interface AnnotationUsageAnnotation {
                                                                           ElementType enumValue() default ElementType.ANNOTATION_TYPE;
                                                                        }
                                                                        """)
                   .withCodeToCompile("AnnotationUsageExample.java", """
                                                                     @AnnotationUsageAnnotation
                                                                     public class AnnotationUsageExample {}
                                                                     """)
                   .compile();
   }

   @Test
   void annotationUsageValue()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("AnnotationUsageExample");
                               Ap.AnnotationUsage defaultValues = cClass.getAnnotationUsages().get(0);

                               Ap.Enum retentionPolicy = context.getEnumOrThrow("java.lang.annotation.RetentionPolicy");
                               Ap.EnumConstant classPolicy = retentionPolicy.getEnumConstantOrThrow("CLASS");

                               Ap.AnnotationUsage defaultUsage = (Ap.AnnotationUsage) defaultValues.getValueOrThrow("annotationUsageValue")
                                                                                                   .getValue();
                               Ap.AnnotationValue defaultRetentionValue = defaultUsage.getValueOrThrow("value");
                               assertEquals(classPolicy, defaultRetentionValue.getValue());
                            })
                   .withCodeToCompile("AnnotationUsageAnnotation.java", """
                                                                        import java.lang.annotation.Retention;
                                                                        import java.lang.annotation.RetentionPolicy;
                                                                        
                                                                        @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                                                        public @interface AnnotationUsageAnnotation {
                                                                           Retention annotationUsageValue() default @Retention(RetentionPolicy.CLASS);
                                                                        }
                                                                        """)
                   .withCodeToCompile("AnnotationUsageExample.java", """
                                                                     @AnnotationUsageAnnotation
                                                                     public class AnnotationUsageExample {}
                                                                     """)
                   .compile();
   }

   @Test
   void listOfValues()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("AnnotationUsageExample");
                               Ap.AnnotationUsage defaultValues = cClass.getAnnotationUsages().get(0);

                               //noinspection unchecked
                               List<Ap.AnnotationValue> values = (List<Ap.AnnotationValue>) defaultValues.getValueOrThrow("asListOfValues")
                                                                                                         .getValue();
                               List<Object> list = values.stream().map(Ap.AnnotationValue::getValue).toList();
                            })
                   .withCodeToCompile("AnnotationUsageAnnotation.java", """
                                                                        @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                                                        public @interface AnnotationUsageAnnotation {
                                                                           char[] asListOfValues() default {'b', 'c'};
                                                                        }
                                                                        """)
                   .withCodeToCompile("AnnotationUsageExample.java", """
                                                                     @AnnotationUsageAnnotation
                                                                     public class AnnotationUsageExample {}
                                                                     """)
                   .compile();
   }

   @Test
   void isDefault()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("AnnotationUsageExample");
                               Ap.AnnotationUsage defaultValues = cClass.getAnnotationUsages().get(0);

                               assertTrue(defaultValues.getValues().values()
                                                       .stream()
                                                       .allMatch(Ap.AnnotationValue::isDefault));
                            })
                   .withCodeToCompile("AnnotationUsageAnnotation.java", """
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
                                                                           ElementType enumValue() default ElementType.ANNOTATION_TYPE;
                                                                           Retention annotationUsageValue() default @Retention(RetentionPolicy.CLASS);
                                                                           char[] asListOfValues() default {'b', 'c'};
                                                                        }
                                                                        """)
                   .withCodeToCompile("AnnotationUsageExample.java", """
                                                                     @AnnotationUsageAnnotation
                                                                     public class AnnotationUsageExample {}
                                                                     """)
                   .compile();
   }

   @Test
   void overwriteValue()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class cClass = context.getClassOrThrow("AnnotationUsageExample");
                               Ap.Field field = cClass.getFieldOrThrow("testField");
                               Ap.AnnotationUsage overwrittenStringValue = field.getAnnotationUsages().get(0);
                               Ap.AnnotationValue annotationValue = overwrittenStringValue.getValueOrThrow("stingValue");
                               assertFalse(annotationValue.isDefault());
                               assertEquals("custom Value", annotationValue.getValue());
                            })
                   .withCodeToCompile("AnnotationUsageAnnotation.java", """
                                                                        @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
                                                                        public @interface AnnotationUsageAnnotation {
                                                                           String stingValue() default "string Value";
                                                                        }
                                                                        """)
                   .withCodeToCompile("AnnotationUsageExample.java", """
                                                                     public class AnnotationUsageExample {
                                                                        @AnnotationUsageAnnotation(stingValue = "custom Value")
                                                                        private String testField;
                                                                     }
                                                                     """)
                   .compile();
   }
}