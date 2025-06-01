package io.determann.shadow.api.dsl.generic;

import io.determann.shadow.api.dsl.class_.ClassRenderable;
import io.determann.shadow.api.dsl.interface_.InterfaceRenderable;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Interface;

public interface GenericExtendsStep
      extends GenericRenderable
{
   GenericAndExtendsStep extends_(String bound);

   GenericAndExtendsStep extends_(C_Class bound);

   GenericAndExtendsStep extends_(ClassRenderable bound);

   GenericAndExtendsStep extends_(C_Interface bound);

   GenericAndExtendsStep extends_(InterfaceRenderable bound);

   GenericAndExtendsStep extends_(C_Generic bound);

   GenericAndExtendsStep extends_(GenericRenderable bound);
}
