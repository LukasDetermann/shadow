package io.determann.shadow.api.dsl.package_;

import io.determann.shadow.api.shadow.type.C_Annotation;

public interface PackageAnnotateStep
      extends PackageNameStep
{
   PackageAnnotateStep annotate(String... annotation);

   PackageAnnotateStep annotate(C_Annotation... annotation);
}
