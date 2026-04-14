package com.derivandi.api.dsl.parameter;

import org.jetbrains.annotations.Contract;

public interface ParameterNameStep
{
   @Contract(value = "_ -> new", pure = true)
   ParameterVarargsStep name(String name);
}
