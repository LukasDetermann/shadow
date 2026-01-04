package io.determann.shadow.api.annotation_processing.dsl.record;

import io.determann.shadow.api.annotation_processing.dsl.interface_.InterfaceRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface RecordImplementsStep
      extends RecordBodyStep
{
   @Contract(value = "_ -> new", pure = true)
   RecordImplementsStep implements_(String... interfaces);

   @Contract(value = "_ -> new", pure = true)
   default RecordImplementsStep implements_(InterfaceRenderable... interfaces)
   {
      return implements_(Arrays.asList(interfaces));
   }

   @Contract(value = "_ -> new", pure = true)
   RecordImplementsStep implements_(List<? extends InterfaceRenderable> interfaces);
}