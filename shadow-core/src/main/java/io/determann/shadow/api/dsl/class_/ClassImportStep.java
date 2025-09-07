package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.import_.ImportRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ClassImportStep
      extends ClassJavaDocStep
{
   /// [io.determann.shadow.api.dsl.RenderingConfigurationBuilder#withoutAutomaticImports(] To disable automatic importing
   @Contract(value = "_ -> new", pure = true)
   ClassImportStep import_(String... name);

   /// [io.determann.shadow.api.dsl.RenderingConfigurationBuilder#withoutAutomaticImports(] To disable automatic importing
   @Contract(value = "_ -> new", pure = true)
   default ClassImportStep import_(ImportRenderable... imports)
   {
      return import_(Arrays.asList(imports));
   }

   /// [io.determann.shadow.api.dsl.RenderingConfigurationBuilder#withoutAutomaticImports(] To disable automatic importing
   @Contract(value = "_ -> new", pure = true)
   ClassImportStep import_(List<? extends ImportRenderable> imports);
}