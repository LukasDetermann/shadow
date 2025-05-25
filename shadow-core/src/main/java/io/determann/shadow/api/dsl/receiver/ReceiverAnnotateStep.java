package io.determann.shadow.api.dsl.receiver;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.shadow.C_AnnotationUsage;

public interface ReceiverAnnotateStep extends ReceiverRenderable
{
   ReceiverAnnotateStep annotate(String... annotation);

   ReceiverAnnotateStep annotate(C_AnnotationUsage... annotation);

   ReceiverAnnotateStep annotate(AnnotationUsageRenderable... annotation);
}
