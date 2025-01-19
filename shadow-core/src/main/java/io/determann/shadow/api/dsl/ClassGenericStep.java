package io.determann.shadow.api.dsl;

import io.determann.shadow.api.shadow.type.C_Generic;

public interface ClassGenericStep extends ClassExtendsStep
{
   ClassGenericStep generic(String... generics);

   ClassGenericStep generic(C_Generic... generics);
}
