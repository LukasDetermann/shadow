package io.determann.shadow.api.dsl.interface_;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;

import java.util.Arrays;
import java.util.List;

public interface InterfacePermitsStep
      extends InterfaceBodyStep
{
   InterfacePermitsStep permits(String... declared);

   default InterfacePermitsStep permits(DeclaredRenderable... declared)
   {
      return permits(Arrays.asList(declared));
   }

   InterfacePermitsStep permits(List<? extends DeclaredRenderable> declared);
}