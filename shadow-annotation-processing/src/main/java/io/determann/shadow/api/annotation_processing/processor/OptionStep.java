package io.determann.shadow.api.annotation_processing.processor;

import org.jetbrains.annotations.Contract;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface OptionStep<OPTIONS>
      extends ProcessorStep<ProcessingCallback<Context<OPTIONS>>>
{
   @Contract(value = "_,_,_ -> new", pure = true)
   <OPTION> OptionStep<OPTIONS> withOption(String name,
                                           Function<String, OPTION> converter,
                                           BiConsumer<OPTIONS, OPTION> setter);

   @Contract(value = "_,_ -> new", pure = true)
   default OptionStep<OPTIONS> withOption(String name,
                                          BiConsumer<OPTIONS, String> setter)
   {
      return withOption(name, Function.identity(), setter);
   }
}