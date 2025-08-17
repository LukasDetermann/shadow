package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ClassPermitsStep
      extends ClassBodyStep
{
   @Contract(value = "_ -> new", pure = true)
   ClassPermitsStep permits(String... declared);

   @Contract(value = "_ -> new", pure = true)
   default ClassPermitsStep permits(DeclaredRenderable... declared)
   {
      return permits(Arrays.asList(declared));
   }

   @Contract(value = "_ -> new", pure = true)
   ClassPermitsStep permits(List<? extends DeclaredRenderable> declared);
}