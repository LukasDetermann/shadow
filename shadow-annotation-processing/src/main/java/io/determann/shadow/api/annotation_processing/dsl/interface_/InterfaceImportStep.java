package io.determann.shadow.api.annotation_processing.dsl.interface_;

import io.determann.shadow.api.annotation_processing.dsl.RenderingConfigurationBuilder;
import io.determann.shadow.api.annotation_processing.dsl.import_.ImportRenderable;
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