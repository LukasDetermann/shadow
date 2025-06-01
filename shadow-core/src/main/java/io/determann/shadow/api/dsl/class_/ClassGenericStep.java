package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.generic.GenericRenderable;
import io.determann.shadow.api.shadow.type.C_Generic;

public interface ClassGenericStep extends ClassExtendsStep
{
   ClassGenericStep generic(String... generics);

   ClassGenericStep generic(C_Generic... generics);

   ClassGenericStep generic(GenericRenderable... generics);
}
