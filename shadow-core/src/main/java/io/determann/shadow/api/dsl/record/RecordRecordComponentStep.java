package io.determann.shadow.api.dsl.record;

import io.determann.shadow.api.C;

import java.util.Arrays;
import java.util.List;

public interface RecordRecordComponentStep
      extends RecordGenericStep
{
   RecordRecordComponentStep component(String... recordComponent);

   default RecordRecordComponentStep component(C.RecordComponent... recordComponent)
   {
      return component(Arrays.asList(recordComponent));
   }

   RecordRecordComponentStep component(List<? extends C.RecordComponent> recordComponent);
}
