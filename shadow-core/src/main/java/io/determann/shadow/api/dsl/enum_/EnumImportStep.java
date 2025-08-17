package io.determann.shadow.api.dsl.enum_;

import io.determann.shadow.api.dsl.import_.ImportRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface EnumImportStep
      extends EnumJavaDocStep
{
   @Contract(value = "_ -> new", pure = true)
   EnumImportStep import_(String... name);

   @Contract(value = "_ -> new", pure = true)
   default EnumImportStep import_(ImportRenderable... imports)
   {
      return import_(Arrays.asList(imports));
   }

   @Contract(value = "_ -> new", pure = true)
   EnumImportStep import_(List<? extends ImportRenderable> imports);
}