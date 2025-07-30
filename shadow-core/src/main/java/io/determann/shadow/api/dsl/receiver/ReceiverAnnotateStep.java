package io.determann.shadow.api.dsl.receiver;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;

import java.util.Arrays;
import java.util.List;

public interface ReceiverAnnotateStep extends ReceiverRenderable
{
   ReceiverAnnotateStep annotate(String... annotation);

   default ReceiverAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   ReceiverAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
