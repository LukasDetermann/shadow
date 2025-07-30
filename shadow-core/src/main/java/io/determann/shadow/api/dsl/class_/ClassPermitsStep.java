package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;

import java.util.Arrays;
import java.util.List;

public interface ClassPermitsStep
      extends ClassBodyStep
{
   ClassPermitsStep permits(String... declared);

   default ClassPermitsStep permits(DeclaredRenderable... declared)
   {
      return permits(Arrays.asList(declared));
   }

   ClassPermitsStep permits(List<? extends DeclaredRenderable> declared);
}