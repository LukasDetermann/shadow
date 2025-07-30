package io.determann.shadow.api.dsl.enum_;

import io.determann.shadow.api.dsl.class_.ClassRenderable;

public interface EnumExtendsStep
      extends EnumImplementsStep
{
   EnumImplementsStep extends_(String aClass);

   EnumImplementsStep extends_(ClassRenderable aClass);
}
