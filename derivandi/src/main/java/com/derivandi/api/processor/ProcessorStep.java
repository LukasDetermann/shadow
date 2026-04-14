package com.derivandi.api.processor;


import org.jetbrains.annotations.Contract;

public interface ProcessorStep<PROCESSING_CALLBACK>
{
   @Contract(value = "_ -> new", pure = true)
   HandlerStep process(PROCESSING_CALLBACK processingCallback);
}
