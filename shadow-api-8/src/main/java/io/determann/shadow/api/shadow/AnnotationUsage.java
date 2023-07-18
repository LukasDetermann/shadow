package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotationvalue.AnnotationValue;
import io.determann.shadow.api.metadata.JdkApi;

import javax.lang.model.element.AnnotationMirror;
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

   AnnotationValue getValueOrThrow(String methodName);

   Optional<AnnotationValue> getValue(String methodName);

   Annotation getAnnotation();

   @JdkApi
   AnnotationMirror getAnnotationMirror();
}
