package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.shadow.type.C_Class;

public interface ClassExtendsStep extends ClassImplementsStep
{
   ClassImplementsStep extends_(String aClass);

   ClassImplementsStep extends_(C_Class aClass);
}
