package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.dsl.generic.GenericRenderable;
import io.determann.shadow.api.shadow.type.C_Generic;

public interface ConstructorGenericStep
      extends ConstructorTypeStep
{
   ConstructorGenericStep generic(String... generic);

   ConstructorGenericStep generic(C_Generic... generic);

   ConstructorGenericStep generic(GenericRenderable... generic);
}
