package io.determann.shadow.api.annotation_processing.dsl.annotation;

import io.determann.shadow.api.annotation_processing.dsl.RenderingConfigurationBuilder;
import io.determann.shadow.api.annotation_processing.dsl.import_.ImportRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface AnnotationImportStep
      extends AnnotationJavaDocStep
{
   /// [RenderingConfigurationBuilder#withoutAutomaticImports()] To disable automatic importing
   @Contract(value = "_ -> new", pure = true)
   AnnotationImportStep import_(String... name);

   /// [RenderingConfigurationBuilder#withoutAutomaticImports()] To disable automatic importing
   @Contract(value = "_ -> new", pure = true)
   default AnnotationImportStep import_(ImportRenderable... imports)
   {
      return import_(Arrays.asList(imports));
   }

   /// [RenderingConfigurationBuilder#withoutAutomaticImports()] To disable automatic importing
   @Contract(value = "_ -> new", pure = true)
   AnnotationImportStep import_(List<? extends ImportRenderable> imports);
}