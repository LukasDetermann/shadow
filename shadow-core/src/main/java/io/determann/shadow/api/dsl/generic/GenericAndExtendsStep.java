package io.determann.shadow.api.dsl.generic;

import io.determann.shadow.api.dsl.interface_.InterfaceRenderable;
import io.determann.shadow.api.shadow.type.C_Interface;

public interface GenericAndExtendsStep
      extends GenericRenderable
{
   GenericAndExtendsStep extends_(String bound);

   GenericAndExtendsStep extends_(C_Interface bound);

   GenericAndExtendsStep extends_(InterfaceRenderable bound);
}
