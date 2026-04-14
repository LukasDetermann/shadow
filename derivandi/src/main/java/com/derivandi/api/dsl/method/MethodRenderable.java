package com.derivandi.api.dsl.method;

import com.derivandi.api.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface MethodRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);

   @Contract(value = "_ -> new", pure = true)
   String renderName(RenderingContext renderingContext);
}
