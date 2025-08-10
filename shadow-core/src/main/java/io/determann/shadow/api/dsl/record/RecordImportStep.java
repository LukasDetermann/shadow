package io.determann.shadow.api.dsl.record;

import io.determann.shadow.api.dsl.import_.ImportRenderable;

import java.util.Arrays;
import java.util.List;

public interface RecordImportStep
      extends RecordJavaDocStep
{
   RecordImportStep import_(String... name);

   default RecordImportStep import_(ImportRenderable... imports)
   {
      return import_(Arrays.asList(imports));
   }

   RecordImportStep import_(List<? extends ImportRenderable> imports);
}