package io.determann.shadow.api.dsl.receiver;

import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ReceiverAnnotateStep
      extends ReceiverRenderable
{
   @Contract(value = "_ -> new", pure = true)
   ReceiverAnnotateStep annotate(String... annotation);

   @Contract(value = "_ -> new", pure = true)
   default ReceiverAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return annotate(Arrays.asList(annotation));
   }

   @Contract(value = "_ -> new", pure = true)
   ReceiverAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation);
}
