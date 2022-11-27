package org.determann.shadow.api.shadow;

import org.determann.shadow.api.metadata.JdkApi;
import org.determann.shadow.api.wrapper.AnnotationValueTypeChooser;
import org.jetbrains.annotations.UnmodifiableView;

import javax.lang.model.element.AnnotationMirror;
import java.util.Map;
import java.util.Optional;

/**
 * {@link Annotation} represents the java file for the java concept of an annotation. This on the other hand represents
 * a usage of such an annotation. like
 * {@code
 *
 * @Docmuented("testValue) public class Test{
 * <p>
 * }}
 */
public interface AnnotationUsage extends Annotation
{
   @UnmodifiableView Map<Method, AnnotationValueTypeChooser> getValues();

   AnnotationValueTypeChooser getValueOrThrow(String methodName);

   Optional<AnnotationValueTypeChooser> getValue(String methodName);

   Annotation getAnnotation();

   @JdkApi
   AnnotationMirror getAnnotationMirror();
}
