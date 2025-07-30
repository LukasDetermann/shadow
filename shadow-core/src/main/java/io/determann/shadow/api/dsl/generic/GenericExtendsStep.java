package io.determann.shadow.api.dsl.generic;

import io.determann.shadow.api.dsl.class_.ClassRenderable;
import io.determann.shadow.api.dsl.interface_.InterfaceRenderable;

public interface GenericExtendsStep
      extends GenericRenderable
{
   GenericAndExtendsStep extends_(String bound);

   GenericAndExtendsStep extends_(ClassRenderable bound);

   GenericAndExtendsStep extends_(InterfaceRenderable bound);

   GenericAndExtendsStep extends_(GenericRenderable bound);
}
