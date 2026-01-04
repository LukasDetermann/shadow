package io.determann.shadow.api.annotation_processing.dsl.record;

import io.determann.shadow.api.annotation_processing.dsl.record_component.RecordComponentRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface RecordRecordComponentStep
      extends RecordGenericStep
{
   @Contract(value = "_ -> new", pure = true)
   RecordRecordComponentStep component(String... recordComponent);

   @Contract(value = "_ -> new", pure = true)
   default RecordRecordComponentStep component(RecordComponentRenderable... recordComponent)
   {
      return component(Arrays.asList(recordComponent));
   }

   @Contract(value = "_ -> new", pure = true)
   RecordRecordComponentStep component(List<? extends RecordComponentRenderable> recordComponent);
}
