package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.import_.ImportRenderable;

import java.util.Arrays;
import java.util.List;

public interface ClassImportStep
      extends ClassJavaDocStep
{
   ClassImportStep import_(String... name);

   default ClassImportStep import_(ImportRenderable... imports)
   {
      return import_(Arrays.asList(imports));
   }

   ClassImportStep import_(List<? extends ImportRenderable> imports);
}