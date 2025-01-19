package io.determann.shadow.api.dsl;

import io.determann.shadow.api.shadow.type.C_Class;

public interface ClassExtendsStep extends ClassImplements
{
   ClassExtendsStep extends_(String... classes);

   ClassExtendsStep extends_(C_Class... classes);
}
