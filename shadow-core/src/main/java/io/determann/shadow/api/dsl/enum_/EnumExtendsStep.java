package io.determann.shadow.api.dsl.enum_;

import io.determann.shadow.api.dsl.class_.ClassRenderable;
import org.jetbrains.annotations.Contract;

public interface EnumExtendsStep
      extends EnumImplementsStep
{
   @Contract(value = "_ -> new", pure = true)
   EnumImplementsStep extends_(String aClass);

   @Contract(value = "_ -> new", pure = true)
   EnumImplementsStep extends_(ClassRenderable aClass);
}
