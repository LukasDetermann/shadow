package io.determann.shadow.api.metadata;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The {@link AnnotationProcessingContext} is transient to the java annotation processor api. Meaning you can transition between using the shadow and the jdk api
 * from line to line if you so wish. Places where you can switch between apis are annotated with {@link JdkApi}.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface JdkApi
{
}
