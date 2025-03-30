package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.shadow.type.C_Annotation;

public interface ClassAnnotateStep
      extends ClassModifierStep
{
   ClassAnnotateStep annotate(String... annotation);

   ClassAnnotateStep annotate(C_Annotation... annotation);
}
