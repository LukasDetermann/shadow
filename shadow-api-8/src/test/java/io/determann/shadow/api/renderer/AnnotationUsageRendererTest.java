package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationUsageRendererTest
{
   @Test
   void usage()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               AnnotationUsage annotationUsage = shadowApi.getClassOrThrow("AnnotationUsageExample")
                                                                          .getAnnotationUsages()
                                                                          .get(0);

                               assertEquals(
                                     "@AnnotationUsageAnnotation(stingValue = \"string Value\", booleanValue = false, byteValue = 1, shortValue = 2, intValue = 3, longValue = 4L, charValue = 'a', floatValue = 5.0F, doubleValue = 6.0D, typeValue = String.class, enumConstantValue = java.lang.annotation.ElementType.ANNOTATION_TYPE, annotationUsageValue = @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.CLASS), asListOfValues = {'b', 'c'})",
                                     render(DEFAULT, annotationUsage).usage());

                               assertEquals(
                                     "@AnnotationUsageAnnotation(stingValue = test, booleanValue = test, byteValue = test, shortValue = test, intValue = test, longValue = test, charValue = test, floatValue = test, doubleValue = test, typeValue = test, enumConstantValue = test, annotationUsageValue = test, asListOfValues = test)",
                                     render(DEFAULT, annotationUsage).usage(method -> Optional.of("test")));

                               assertEquals(
                                     "@AnnotationUsageAnnotation(stingValue = \"test\", booleanValue = false, byteValue = 1, shortValue = 2, intValue = 3, longValue = 4L, charValue = 'a', floatValue = 5.0F, doubleValue = 6.0D, typeValue = String.class, enumConstantValue = java.lang.annotation.ElementType.ANNOTATION_TYPE, annotationUsageValue = @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.CLASS), asListOfValues = {'b', 'c'})",
                                     render(DEFAULT, annotationUsage).usage(method ->
                                                                   {
                                                                      if (method.getReturnType()
                                                                                .representsSameType(shadowApi.getClassOrThrow("java.lang.String")))
                                                                      {
                                                                         return Optional.of("\"test\"");
                                                                      }
                                                                      return Optional.empty();
                                                                   }));
                            })
                   .withCodeToCompile("AnnotationUsageAnnotation.java", "import java.lang.annotation.ElementType;\n" +
                                                                        "import java.lang.annotation.Retention;\n" +
                                                                        "import java.lang.annotation.RetentionPolicy;\n" +
                                                                        "\n" +
                                                                        "public @interface AnnotationUsageAnnotation {\n" +
                                                                        "   String stingValue() default \"string Value\";\n" +
                                                                        "   boolean booleanValue() default false;\n" +
                                                                        "   byte byteValue() default 1;\n" +
                                                                        "   short shortValue() default 2;\n" +
                                                                        "   int intValue() default 3;\n" +
                                                                        "   long longValue() default 4L;\n" +
                                                                        "   char charValue() default 'a';\n" +
                                                                        "   float floatValue() default 5f;\n" +
                                                                        "   double doubleValue() default 6D;\n" +
                                                                        "   Class<String> typeValue() default String.class;\n" +
                                                                        "   ElementType enumConstantValue() default ElementType.ANNOTATION_TYPE;\n" +
                                                                        "   Retention annotationUsageValue() default @Retention(RetentionPolicy.CLASS);\n" +
                                                                        "   char[] asListOfValues() default {'b', 'c'};\n" +
                                                                        "}\n")
                   .withCodeToCompile("AnnotationUsageExample.java", "@AnnotationUsageAnnotation\n" +
                                                                     "public class AnnotationUsageExample {\n" +
                                                                     "   @AnnotationUsageAnnotation(stingValue = \"custom Value\")\n" +
                                                                     "   private String testField;\n" +
                                                                     "}\n")
                   .compile();
   }
}