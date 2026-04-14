package com.derivandi.api.dsl.record;

import com.derivandi.api.dsl.RenderingConfigurationBuilder;
import com.derivandi.api.dsl.import_.ImportRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface RecordImportStep
      extends RecordJavaDocStep
{
   /// [RenderingConfigurationBuilder#withoutAutomaticImports(] To disable automatic importing
   @Contract(value = "_ -> new", pure = true)
   RecordImportStep import_(String... name);

   /// [RenderingConfigurationBuilder#withoutAutomaticImports(] To disable automatic importing
   @Contract(value = "_ -> new", pure = true)
   default RecordImportStep import_(ImportRenderable... imports)
   {
      return import_(Arrays.asList(imports));
   }

   /// [RenderingConfigurationBuilder#withoutAutomaticImports(] To disable automatic importing
   @Contract(value = "_ -> new", pure = true)
   RecordImportStep import_(List<? extends ImportRenderable> imports);
}