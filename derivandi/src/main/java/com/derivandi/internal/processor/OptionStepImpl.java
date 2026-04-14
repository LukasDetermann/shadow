package com.derivandi.internal.processor;

import com.derivandi.api.processor.Context;
import com.derivandi.api.processor.HandlerStep;
import com.derivandi.api.processor.OptionStep;
import com.derivandi.api.processor.ProcessingCallback;

import javax.lang.model.SourceVersion;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class OptionStepImpl<OPTIONS>
      implements OptionStep<OPTIONS>
{
   private final Supplier<OPTIONS> optionsSupplier;
   private final List<Option<OPTIONS>> options;
   private final SourceVersion sourceVersion;

   OptionStepImpl(SourceVersion sourceVersion, Supplier<OPTIONS> optionsSupplier)
   {
      this.sourceVersion = sourceVersion;
      this.optionsSupplier = optionsSupplier;
      this.options = new ArrayList<>();
   }

   private OptionStepImpl(OptionStepImpl<OPTIONS> other)
   {
      this.optionsSupplier = other.optionsSupplier;
      this.options = new ArrayList<>(other.options);
      this.sourceVersion = other.sourceVersion;
   }

   @Override
   public <OPTION> OptionStep<OPTIONS> withOption(String name, Function<String, OPTION> converter, BiConsumer<OPTIONS, OPTION> setter)
   {
      Objects.requireNonNull(name);
      Objects.requireNonNull(converter);
      Objects.requireNonNull(setter);
      OptionStepImpl<OPTIONS> copy = new OptionStepImpl<>(this);
      copy.options.add(new Option<>(name, converter, setter));
      return copy;
   }

   @Override
   public HandlerStep process(ProcessingCallback<Context<OPTIONS>> optionsProcessingCallback)
   {
      return new HandlerStepImpl<>(getSupportedOptions(),
                                   this::createOptions,
                                   sourceVersion,
                                   optionsProcessingCallback);
   }

   private Set<String> getSupportedOptions()
   {
      return options.stream().map(Option::name).collect(Collectors.toUnmodifiableSet());
   }

   private OPTIONS createOptions(Map<String, String> optionMap)
   {
      OPTIONS container = optionsSupplier.get();
      for (Option<OPTIONS> option : options)
      {
         String value = optionMap.get(option.name);
         Object converted = option.converter.apply(value);
         //noinspection unchecked
         ((BiConsumer<OPTIONS, Object>) option.setter).accept(container, converted);
      }
      return container;
   }

   private record Option<OPTIONS>(String name,
                                  Function<String, ?> converter,
                                  BiConsumer<OPTIONS, ?> setter) {}
}
