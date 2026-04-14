package com.derivandi.api.processor;

public interface Context<Options>
      extends SimpleContext
{
   Options getOptions();
}