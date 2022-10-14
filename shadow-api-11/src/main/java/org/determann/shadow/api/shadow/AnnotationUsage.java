package org.determann.shadow.api.shadow;

import org.determann.shadow.api.metadata.JdkApi;
import org.determann.shadow.api.wrapper.AnnotationValueTypeChooser;
import org.jetbrains.annotations.UnmodifiableView;

import javax.lang.model.element.AnnotationMirror;
import java.util.Map;

/**
 * {@link Annotation} represents the java file for the java concept of an annotation. This on the other hand represents
 * a usage of such an annotation. like
 * {@code
 * @Docmuented("testValue)
 * public class Test{
 *
 * }}
 */
public interface AnnotationUsage extends Annotation
{
   @UnmodifiableView Map<Method, AnnotationValueTypeChooser> getValues();

   AnnotationValueTypeChooser getValue(String methodName);

   Annotation getAnnotation();

   @JdkApi
   AnnotationMirror getAnnotationMirror();
}
