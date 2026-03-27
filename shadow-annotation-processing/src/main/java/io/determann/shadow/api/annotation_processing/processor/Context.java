package io.determann.shadow.api.annotation_processing.processor;

public interface Context<Options>
      extends SimpleContext
{
   Options getOptions();
}