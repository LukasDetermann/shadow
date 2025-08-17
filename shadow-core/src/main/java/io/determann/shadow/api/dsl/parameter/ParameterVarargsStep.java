package io.determann.shadow.api.dsl.parameter;

import org.jetbrains.annotations.Contract;

public interface ParameterVarargsStep extends ParameterRenderable
{
   @Contract(value = " -> new", pure = true)
   ParameterRenderable varArgs();
}
