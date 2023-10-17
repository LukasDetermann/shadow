package io.determann.shadow.api.test;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;

/**
 * A Callback that will be executed in an annotation processor
 */
@FunctionalInterface
public interface ProcessingCallback
{
   void process(AnnotationProcessingContext annotationProcessingContext);
}
