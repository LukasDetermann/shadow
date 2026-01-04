package io.determann.shadow.api.annotation_processing.dsl.enum_;

import io.determann.shadow.api.annotation_processing.dsl.interface_.InterfaceRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface EnumImplementsStep
      extends EnumEnumConstantStep
{
   @Contract(value = "_ -> new", pure = true)
   EnumImplementsStep implements_(String... interfaces);

   @Contract(value = "_ -> new", pure = true)
   default EnumImplementsStep implements_(InterfaceRenderable... interfaces)
   {
      return implements_(Arrays.asList(interfaces));
   }

   @Contract(value = "_ -> new", pure = true)
   EnumImplementsStep implements_(List<? extends InterfaceRenderable> interfaces);
}