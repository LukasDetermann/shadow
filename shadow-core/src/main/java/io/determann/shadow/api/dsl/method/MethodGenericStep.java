package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.dsl.generic.GenericRenderable;
import io.determann.shadow.api.shadow.type.C_Generic;

public interface MethodGenericStep
      extends MethodResultStep
{
   MethodGenericStep generic(String... generic);

   MethodGenericStep generic(C_Generic... generic);

   MethodGenericStep generic(GenericRenderable... generic);
}
