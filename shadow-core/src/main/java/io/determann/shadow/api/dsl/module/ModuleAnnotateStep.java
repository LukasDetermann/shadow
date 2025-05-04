package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.shadow.type.C_Annotation;

public interface ModuleAnnotateStep
      extends ModuleNameStep
{
   ModuleAnnotateStep annotate(String... annotation);

   ModuleAnnotateStep annotate(C_Annotation... annotation);
}
