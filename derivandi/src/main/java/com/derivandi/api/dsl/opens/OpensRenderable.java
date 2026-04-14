package com.derivandi.api.dsl.opens;

import com.derivandi.api.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface OpensRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);
}
