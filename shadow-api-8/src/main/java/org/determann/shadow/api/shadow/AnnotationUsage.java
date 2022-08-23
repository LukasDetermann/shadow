package org.determann.shadow.api.shadow;

import org.determann.shadow.api.JdkApi;
import org.determann.shadow.api.wrapper.AnnotationValueTypeChooser;

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
   Map<Method, AnnotationValueTypeChooser> getValues();

   AnnotationValueTypeChooser getValue(String methodName);

   @JdkApi
   AnnotationMirror getAnnotationMirror();
}
