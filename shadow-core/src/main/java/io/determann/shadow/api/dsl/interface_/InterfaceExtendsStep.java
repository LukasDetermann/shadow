package io.determann.shadow.api.dsl.interface_;

import java.util.Arrays;
import java.util.List;

public interface InterfaceExtendsStep
      extends InterfacePermitsStep
{
   InterfaceExtendsStep extends_(String... interfaces);

   default InterfaceExtendsStep extends_(InterfaceRenderable... interfaces)
   {
      return extends_(Arrays.asList(interfaces));
   }

   InterfaceExtendsStep extends_(List<? extends InterfaceRenderable> interfaces);
}