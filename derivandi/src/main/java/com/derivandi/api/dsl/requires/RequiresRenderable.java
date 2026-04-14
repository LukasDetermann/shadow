package com.derivandi.api.dsl.requires;

import com.derivandi.api.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface RequiresRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);
}
