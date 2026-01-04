package io.determann.shadow.api.annotation_processing.dsl.generic;

import io.determann.shadow.api.annotation_processing.dsl.class_.ClassRenderable;
import io.determann.shadow.api.annotation_processing.dsl.interface_.InterfaceRenderable;
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
