package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.shadow.Operations.EXECUTABLE_GET_RETURN_TYPE;
import static io.determann.shadow.api.shadow.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

class AnnotationUsageRendererTest
{

   public static final Pattern PATTERN = Pattern.compile("([^(),{])+[ = ]([^(),{])+");

   @Test
   void usage()
   {
      ConsistencyTest.compileTime(context -> context.getClassOrThrow("AnnotationUsageExample")
                                                    .getAnnotationUsages()
                                                    .get(0))
                     .runtime(stringClassFunction ->
                              {
                                 Class example = ReflectionAdapter.generalize(stringClassFunction.apply("AnnotationUsageExample"));
                                 return example.getAnnotationUsages().get(0);
                              })
                     .withCode("AnnotationUsageAnnotation.java", """
                           import java.lang.annotation.ElementType;
                           import java.lang.annotation.Retention;
                           import java.lang.annotation.RetentionPolicy;

                           @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
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
                     .withCode("AnnotationUsageExample.java", """
                           @AnnotationUsageAnnotation
                           public class AnnotationUsageExample {
                              @AnnotationUsageAnnotation(stingValue = "custom Value")
                              private String testField;
                           }
                           """)
                     .test(annotationUsage ->
                           {
                              assertEquals(
                                    "@AnnotationUsageAnnotation(stingValue = \"string Value\", booleanValue = false, byteValue = 1, shortValue = 2, intValue = 3, longValue = 4L, charValue = 'a', floatValue = 5.0F, doubleValue = 6.0D, typeValue = String.class, enumConstantValue = java.lang.annotation.ElementType.ANNOTATION_TYPE, annotationUsageValue = @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.CLASS), asListOfValues = {'b', 'c'})",
                                    Renderer.render(RenderingContext.DEFAULT, annotationUsage).usage());

                              assertEquals(
                                    "@AnnotationUsageAnnotation(stingValue = test, booleanValue = test, byteValue = test, shortValue = test, intValue = test, longValue = test, charValue = test, floatValue = test, doubleValue = test, typeValue = test, enumConstantValue = test, annotationUsageValue = test, asListOfValues = test)",
                                    render(RenderingContext.DEFAULT, annotationUsage).usage(method -> Optional.of("test")));

                              assertEquals(
                                    "@AnnotationUsageAnnotation(stingValue = \"test\", booleanValue = false, byteValue = 1, shortValue = 2, intValue = 3, longValue = 4L, charValue = 'a', floatValue = 5.0F, doubleValue = 6.0D, typeValue = String.class, enumConstantValue = java.lang.annotation.ElementType.ANNOTATION_TYPE, annotationUsageValue = @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.CLASS), asListOfValues = {'b', 'c'})",
                                    render(RenderingContext.DEFAULT, annotationUsage)
                                          .usage(method ->
                                                       convert(requestOrThrow(method, EXECUTABLE_GET_RETURN_TYPE))
                                                             .toClass()
                                                             .filter(aClass -> requestOrThrow(aClass, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals("java.lang.String"))
                                                             .map(aClass -> "\"test\"")));
                           });
   }

   private static void assertEquals(String expected, String actual)
   {
      List<String> expectedRendering = split(expected);
      expectedRendering.sort(Comparator.naturalOrder());

      List<String> actualRendering = split(actual);
      actualRendering.sort(Comparator.naturalOrder());

      Assertions.assertEquals(expectedRendering, actualRendering);
   }

   private static List<String> split(String toSplit)
   {
      Matcher matcher = PATTERN.matcher(toSplit);
      List<String> result = new ArrayList<>();
      while (matcher.find())
      {
         result.add(matcher.group().trim());
      }
      return result;
   }
}