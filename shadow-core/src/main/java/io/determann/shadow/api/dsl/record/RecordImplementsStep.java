package io.determann.shadow.api.dsl.record;

import io.determann.shadow.api.dsl.interface_.InterfaceRenderable;

import java.util.Arrays;
import java.util.List;

public interface RecordImplementsStep
      extends RecordBodyStep
{
   RecordImplementsStep implements_(String... interfaces);

   default RecordImplementsStep implements_(InterfaceRenderable... interfaces)
   {
      return implements_(Arrays.asList(interfaces));
   }

   RecordImplementsStep implements_(List<? extends InterfaceRenderable> interfaces);
}