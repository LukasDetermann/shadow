package io.determann.shadow.api.dsl.record;

import io.determann.shadow.api.dsl.import_.ImportRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface RecordImportStep
      extends RecordJavaDocStep
{
   @Contract(value = "_ -> new", pure = true)
   RecordImportStep import_(String... name);

   @Contract(value = "_ -> new", pure = true)
   default RecordImportStep import_(ImportRenderable... imports)
   {
      return import_(Arrays.asList(imports));
   }

   @Contract(value = "_ -> new", pure = true)
   RecordImportStep import_(List<? extends ImportRenderable> imports);
}