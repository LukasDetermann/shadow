package io.determann.shadow.api.lang_model.shadow;

import io.determann.shadow.api.lang_model.shadow.structure.LM_Method;
import io.determann.shadow.api.lang_model.shadow.type.LM_Annotation;
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
public interface LM_AnnotationUsage extends C_AnnotationUsage
{
   Map<LM_Method, LM_AnnotationValue> getValues();

   default LM_AnnotationValue getValueOrThrow(String methodName)
   {
      return getValue(methodName).orElseThrow();
   }

   default Optional<LM_AnnotationValue> getValue(String methodName)
   {
      return getValues().entrySet()
                        .stream()
                        .filter(entry -> requestOrThrow(entry.getKey(), NAMEABLE_GET_NAME).equals(methodName))
                        .map(Map.Entry::getValue)
                        .findAny();
   }

   LM_Annotation getAnnotation();
}
