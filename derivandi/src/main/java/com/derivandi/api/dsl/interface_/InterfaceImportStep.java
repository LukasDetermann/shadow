package com.derivandi.api.dsl.interface_;

import com.derivandi.api.dsl.RenderingConfigurationBuilder;
import com.derivandi.api.dsl.import_.ImportRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface InterfaceImportStep
      extends InterfaceJavaDocStep
{
   /// [RenderingConfigurationBuilder#withoutAutomaticImports(] To disable automatic importing
   @Contract(value = "_ -> new", pure = true)
   InterfaceImportStep import_(String... name);

   /// [RenderingConfigurationBuilder#withoutAutomaticImports(] To disable automatic importing
   @Contract(value = "_ -> new", pure = true)
   default InterfaceImportStep import_(ImportRenderable... imports)
   {
      return import_(Arrays.asList(imports));
   }

   /// [RenderingConfigurationBuilder#withoutAutomaticImports(] To disable automatic importing
   @Contract(value = "_ -> new", pure = true)
   InterfaceImportStep import_(List<? extends ImportRenderable> imports);
}