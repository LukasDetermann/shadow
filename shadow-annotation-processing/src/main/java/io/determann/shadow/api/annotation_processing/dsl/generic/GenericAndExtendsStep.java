package io.determann.shadow.api.annotation_processing.dsl.generic;

import io.determann.shadow.api.annotation_processing.dsl.interface_.InterfaceRenderable;
import org.jetbrains.annotations.Contract;

public interface GenericAndExtendsStep
      extends GenericRenderable
{
   @Contract(value = "_ -> new", pure = true)
   GenericAndExtendsStep extends_(String bound);

   @Contract(value = "_ -> new", pure = true)
   GenericAndExtendsStep extends_(InterfaceRenderable bound);
}
