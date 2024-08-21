package io.determann.shadow.api.lang_model.shadow;

import io.determann.shadow.api.lang_model.shadow.structure.MethodLangModel;
import io.determann.shadow.api.lang_model.shadow.type.AnnotationLangModel;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.type.Annotation;

import java.util.Map;
import java.util.Optional;

import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;

/**
 * {@link Annotation} represents the java file for the java concept of an annotation. This on the other hand represents
 * a usage of such an annotation. like <br>
 * {@code @Documented("testValue) public class Test{ }}
 */
public interface AnnotationUsageLangModel extends AnnotationUsage
{
   Map<MethodLangModel, AnnotationValueLangModel> getValues();

   default AnnotationValueLangModel getValueOrThrow(String methodName)
   {
      return getValue(methodName).orElseThrow();
   }

   default Optional<AnnotationValueLangModel> getValue(String methodName)
   {
      return getValues().entrySet()
                        .stream()
                        .filter(entry -> requestOrThrow(entry.getKey(), NAMEABLE_GET_NAME).equals(methodName))
                        .map(Map.Entry::getValue)
                        .findAny();
   }

   AnnotationLangModel getAnnotation();
}
