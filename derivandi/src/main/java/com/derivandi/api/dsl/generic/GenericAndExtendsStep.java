package com.derivandi.api.dsl.generic;

import com.derivandi.api.dsl.interface_.InterfaceRenderable;
import org.jetbrains.annotations.Contract;

public interface GenericAndExtendsStep
      extends GenericRenderable
{
   @Contract(value = "_ -> new", pure = true)
   GenericAndExtendsStep extends_(String bound);

   @Contract(value = "_ -> new", pure = true)
   GenericAndExtendsStep extends_(InterfaceRenderable bound);
}
