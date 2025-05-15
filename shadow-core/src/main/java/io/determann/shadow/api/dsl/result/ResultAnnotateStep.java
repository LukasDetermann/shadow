package io.determann.shadow.api.dsl.result;

import io.determann.shadow.api.shadow.type.C_Annotation;

public interface ResultAnnotateStep
      extends ResultTypeStep
{
   ResultAnnotateStep annotate(String... annotation);

   ResultAnnotateStep annotate(C_Annotation... annotation);
}
