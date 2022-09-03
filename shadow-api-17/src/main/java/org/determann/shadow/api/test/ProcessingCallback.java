package org.determann.shadow.api.test;

import org.determann.shadow.api.ShadowApi;

/**
 * A Callback that will be executed in an annotation processor
 */
@FunctionalInterface
public interface ProcessingCallback
{
   void process(ShadowApi shadowApi);
}
