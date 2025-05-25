package io.determann.shadow.api.dsl.record_component;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.shadow.C_AnnotationUsage;

public interface RecordComponentAnnotateStep extends RecordComponentNameStep
{
   RecordComponentAnnotateStep annotate(String... annotation);

   RecordComponentAnnotateStep annotate(C_AnnotationUsage... annotation);

   RecordComponentAnnotateStep annotate(AnnotationUsageRenderable... annotation);
}
