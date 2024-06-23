package io.determann.shadow.api.lang_model.query;

import io.determann.shadow.api.annotationvalue.AnnotationValue;
import io.determann.shadow.api.shadow.Annotation;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Method;

import java.util.Map;
import java.util.Optional;

import static io.determann.shadow.meta_meta.Operations.NAMEABLE_NAME;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

/**
 * {@link Annotation} represents the java file for the java concept of an annotation. This on the other hand represents
 * a usage of such an annotation. like <br>
 * {@code @Documented("testValue) public class Test{ }}
 */
public interface AnnotationUsageLangModel extends AnnotationUsage
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
                        .filter(entry -> requestOrThrow(entry.getKey(), NAMEABLE_NAME).equals(methodName))
                        .map(Map.Entry::getValue)
                        .findAny();
   }

   Annotation getAnnotation();
}
