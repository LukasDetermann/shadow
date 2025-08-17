package io.determann.shadow.api.dsl.annotation;

import io.determann.shadow.api.dsl.import_.ImportRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface AnnotationImportStep
      extends AnnotationJavaDocStep
{
   @Contract(value = "_ -> new", pure = true)
   AnnotationImportStep import_(String... name);

   @Contract(value = "_ -> new", pure = true)
   default AnnotationImportStep import_(ImportRenderable... imports)
   {
      return import_(Arrays.asList(imports));
   }

   @Contract(value = "_ -> new", pure = true)
   AnnotationImportStep import_(List<? extends ImportRenderable> imports);
}