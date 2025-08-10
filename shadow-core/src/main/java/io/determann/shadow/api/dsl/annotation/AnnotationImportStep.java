package io.determann.shadow.api.dsl.annotation;

import io.determann.shadow.api.dsl.import_.ImportRenderable;

import java.util.Arrays;
import java.util.List;

public interface AnnotationImportStep
      extends AnnotationJavaDocStep
{
   AnnotationImportStep import_(String... name);

   default AnnotationImportStep import_(ImportRenderable... imports)
   {
      return import_(Arrays.asList(imports));
   }

   AnnotationImportStep import_(List<? extends ImportRenderable> imports);
}