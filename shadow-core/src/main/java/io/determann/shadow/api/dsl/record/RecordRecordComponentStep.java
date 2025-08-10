package io.determann.shadow.api.dsl.record;

import io.determann.shadow.api.dsl.record_component.RecordComponentRenderable;

import java.util.Arrays;
import java.util.List;

public interface RecordRecordComponentStep
      extends RecordGenericStep
{
   RecordRecordComponentStep component(String... recordComponent);

   default RecordRecordComponentStep component(RecordComponentRenderable... recordComponent)
   {
      return component(Arrays.asList(recordComponent));
   }

   RecordRecordComponentStep component(List<? extends RecordComponentRenderable> recordComponent);
}
