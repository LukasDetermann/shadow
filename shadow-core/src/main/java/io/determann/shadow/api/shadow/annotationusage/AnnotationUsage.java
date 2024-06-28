package io.determann.shadow.api.shadow.annotationusage;

import io.determann.shadow.api.ImplementationDefined;
import io.determann.shadow.api.shadow.type.Annotation;

/**
 * {@link Annotation} represents the java file for the java concept of an annotation. This on the other hand represents
 * a usage of such an annotation. like <br>
 * {@code @Documented("testValue) public class Test{ }}
 */
public interface AnnotationUsage extends ImplementationDefined
{
}
