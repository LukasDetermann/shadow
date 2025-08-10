package io.determann.shadow.api.dsl.enum_;

import io.determann.shadow.api.dsl.import_.ImportRenderable;

import java.util.Arrays;
import java.util.List;

public interface EnumImportStep
      extends EnumJavaDocStep
{
   EnumImportStep import_(String... name);

   default EnumImportStep import_(ImportRenderable... imports)
   {
      return import_(Arrays.asList(imports));
   }

   EnumImportStep import_(List<? extends ImportRenderable> imports);
}