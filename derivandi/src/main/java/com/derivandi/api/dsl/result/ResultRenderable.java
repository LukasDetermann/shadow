package com.derivandi.api.dsl.result;

import com.derivandi.api.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface ResultRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);
}
