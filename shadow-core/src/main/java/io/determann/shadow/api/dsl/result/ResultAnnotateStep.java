package io.determann.shadow.api.dsl.result;

import io.determann.shadow.api.shadow.C_AnnotationUsage;

public interface ResultAnnotateStep
      extends ResultTypeStep
{
   ResultAnnotateStep annotate(String... annotation);

   ResultAnnotateStep annotate(C_AnnotationUsage... annotation);
}
