package io.determann.shadow.api.annotation_processing.dsl.parameter;

import org.jetbrains.annotations.Contract;

public interface ParameterVarargsStep extends ParameterRenderable
{
   @Contract(value = " -> new", pure = true)
   ParameterRenderable varArgs();
}
