package io.determann.shadow.api.annotation_processing.test;

import io.determann.shadow.api.annotation_processing.processor.SimpleContext;

/**
 * A Callback that will be executed in an annotation processor
 */
@FunctionalInterface
public interface ProcessingCallback<CONTEXT extends SimpleContext>
{
   void process(CONTEXT context) throws Exception;
}
