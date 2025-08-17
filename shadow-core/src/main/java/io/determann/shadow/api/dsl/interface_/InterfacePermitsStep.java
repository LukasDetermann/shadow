package io.determann.shadow.api.dsl.interface_;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface InterfacePermitsStep
      extends InterfaceBodyStep
{
   @Contract(value = "_ -> new", pure = true)
   InterfacePermitsStep permits(String... declared);

   @Contract(value = "_ -> new", pure = true)
   default InterfacePermitsStep permits(DeclaredRenderable... declared)
   {
      return permits(Arrays.asList(declared));
   }

   @Contract(value = "_ -> new", pure = true)
   InterfacePermitsStep permits(List<? extends DeclaredRenderable> declared);
}