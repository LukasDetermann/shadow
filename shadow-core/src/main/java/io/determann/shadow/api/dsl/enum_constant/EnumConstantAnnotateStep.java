package io.determann.shadow.api.dsl.enum_constant;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.shadow.C_AnnotationUsage;

public interface EnumConstantAnnotateStep extends EnumConstantNameStep
{
   EnumConstantAnnotateStep annotate(String... annotation);

   EnumConstantAnnotateStep annotate(C_AnnotationUsage... annotation);

   EnumConstantAnnotateStep annotate(AnnotationUsageRenderable... annotation);
}
