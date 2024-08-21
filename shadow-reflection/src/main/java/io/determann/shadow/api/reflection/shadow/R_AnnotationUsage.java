package io.determann.shadow.api.reflection.shadow;

import io.determann.shadow.api.reflection.shadow.structure.R_Method;
import io.determann.shadow.api.reflection.shadow.type.R_Annotation;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.type.C_Annotation;

import java.util.Map;
import java.util.Optional;

import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;

/**
 * {@link C_Annotation} represents the java file for the java concept of an annotation. This on the other hand represents
 * a usage of such an annotation. like <br>
 * {@code @Documented("testValue) public class Test{ }}
 */
public interface R_AnnotationUsage extends C_AnnotationUsage
{
   Map<R_Method, R_AnnotationValue> getValues();

   default R_AnnotationValue getValueOrThrow(String methodName)
   {
      return getValue(methodName).orElseThrow();
   }

   default Optional<R_AnnotationValue> getValue(String methodName)
   {
      return getValues().entrySet()
                        .stream()
                        .filter(entry -> requestOrThrow(entry.getKey(), NAMEABLE_GET_NAME).equals(methodName))
                        .map(Map.Entry::getValue)
                        .findAny();
   }

   R_Annotation getAnnotation();
}
