package com.derivandi.api.dsl.parameter;

import com.derivandi.api.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface ParameterRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);

   @Contract(value = "_ -> new", pure = true)
   String renderName(RenderingContext renderingContext);
}
