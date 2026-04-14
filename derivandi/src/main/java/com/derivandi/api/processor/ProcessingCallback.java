package com.derivandi.api.processor;

/**
 * A Callback that will be executed in an annotation processor
 */
@FunctionalInterface
public interface ProcessingCallback<CONTEXT extends SimpleContext>
{
   void process(CONTEXT context) throws Exception;
}
