package io.determann.shadow.api.annotation_processing.dsl.interface_;

import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface InterfaceExtendsStep
      extends InterfacePermitsStep
{
   @Contract(value = "_ -> new", pure = true)
   InterfaceExtendsStep extends_(String... interfaces);

   @Contract(value = "_ -> new", pure = true)
   default InterfaceExtendsStep extends_(InterfaceRenderable... interfaces)
   {
      return extends_(Arrays.asList(interfaces));
   }

   @Contract(value = "_ -> new", pure = true)
   InterfaceExtendsStep extends_(List<? extends InterfaceRenderable> interfaces);
}