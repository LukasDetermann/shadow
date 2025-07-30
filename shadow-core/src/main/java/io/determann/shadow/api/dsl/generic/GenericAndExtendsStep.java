package io.determann.shadow.api.dsl.generic;

import io.determann.shadow.api.dsl.interface_.InterfaceRenderable;

public interface GenericAndExtendsStep
      extends GenericRenderable
{
   GenericAndExtendsStep extends_(String bound);

   GenericAndExtendsStep extends_(InterfaceRenderable bound);
}
