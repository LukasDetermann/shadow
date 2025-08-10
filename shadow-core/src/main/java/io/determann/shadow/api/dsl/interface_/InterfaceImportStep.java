package io.determann.shadow.api.dsl.interface_;

import io.determann.shadow.api.dsl.import_.ImportRenderable;

import java.util.Arrays;
import java.util.List;

public interface InterfaceImportStep
      extends InterfaceJavaDocStep
{
   InterfaceImportStep import_(String... name);

   default InterfaceImportStep import_(ImportRenderable... imports)
   {
      return import_(Arrays.asList(imports));
   }

   InterfaceImportStep import_(List<? extends ImportRenderable> imports);
}