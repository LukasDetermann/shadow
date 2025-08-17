package io.determann.shadow.api.dsl.generic;

import io.determann.shadow.api.dsl.class_.ClassRenderable;
import io.determann.shadow.api.dsl.interface_.InterfaceRenderable;
import org.jetbrains.annotations.Contract;

public interface GenericExtendsStep
      extends GenericRenderable
{
   @Contract(value = "_ -> new", pure = true)
   GenericAndExtendsStep extends_(String bound);

   @Contract(value = "_ -> new", pure = true)
   GenericAndExtendsStep extends_(ClassRenderable bound);

   @Contract(value = "_ -> new", pure = true)
   GenericAndExtendsStep extends_(InterfaceRenderable bound);

   @Contract(value = "_ -> new", pure = true)
   GenericAndExtendsStep extends_(GenericRenderable bound);
}
