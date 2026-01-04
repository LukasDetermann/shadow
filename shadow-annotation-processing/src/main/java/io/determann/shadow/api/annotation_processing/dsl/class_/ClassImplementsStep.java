package io.determann.shadow.api.annotation_processing.dsl.class_;

import io.determann.shadow.api.annotation_processing.dsl.interface_.InterfaceRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ClassImplementsStep
      extends ClassPermitsStep
{
   @Contract(value = "_ -> new", pure = true)
   ClassImplementsStep implements_(String... interfaces);

   @Contract(value = "_ -> new", pure = true)
   default ClassImplementsStep implements_(InterfaceRenderable... interfaces)
   {
      return implements_(Arrays.asList(interfaces));
   }

   @Contract(value = "_ -> new", pure = true)
   ClassImplementsStep implements_(List<? extends InterfaceRenderable> interfaces);
}