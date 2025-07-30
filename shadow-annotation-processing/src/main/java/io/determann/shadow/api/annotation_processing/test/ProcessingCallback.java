package io.determann.shadow.api.annotation_processing.test;

import io.determann.shadow.api.annotation_processing.AP;

/**
 * A Callback that will be executed in an annotation processor
 */
@FunctionalInterface
public interface ProcessingCallback
{
   void process(AP.Context annotationProcessingContext);
}
