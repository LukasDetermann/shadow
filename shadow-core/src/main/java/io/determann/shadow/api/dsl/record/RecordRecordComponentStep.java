package io.determann.shadow.api.dsl.record;

import io.determann.shadow.api.shadow.structure.C_RecordComponent;

import java.util.Arrays;
import java.util.List;

public interface RecordRecordComponentStep
      extends RecordGenericStep
{
   RecordRecordComponentStep component(String... recordComponent);

   default RecordRecordComponentStep component(C_RecordComponent... recordComponent)
   {
      return component(Arrays.asList(recordComponent));
   }

   RecordRecordComponentStep component(List<? extends C_RecordComponent> recordComponent);
}
