package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotationvalue.AnnotationValue;

import java.util.Map;
import java.util.Optional;

/**
 * {@link Annotation} represents the java file for the java concept of an annotation. This on the other hand represents
 * a usage of such an annotation. like <br>
 * {@code @Documented("testValue) public class Test{ }}
 */
public interface AnnotationUsage extends Annotation
{
   Map<Method, AnnotationValue> getValues();

   default AnnotationValue getValueOrThrow(String methodName)
   {
      return getValue(methodName).orElseThrow();
   }

   default Optional<AnnotationValue> getValue(String methodName)
   {
      return getValues().entrySet()
                        .stream()
                        .filter(entry -> entry.getKey().getName().equals(methodName))
                        .map(Map.Entry::getValue)
                        .findAny();
   }

   Annotation getAnnotation();
}
