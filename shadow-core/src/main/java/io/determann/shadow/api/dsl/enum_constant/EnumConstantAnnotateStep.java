package io.determann.shadow.api.dsl.enum_constant;

import io.determann.shadow.api.shadow.type.C_Annotation;

public interface EnumConstantAnnotateStep extends EnumConstantNameStep
{
   EnumConstantAnnotateStep annotate(String... annotation);

   EnumConstantAnnotateStep annotate(C_Annotation... annotation);
}
