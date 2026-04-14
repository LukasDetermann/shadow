package com.derivandi.api.dsl.generic;

import com.derivandi.api.dsl.ReferenceTypeRenderable;
import com.derivandi.api.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface GenericRenderable
      extends ReferenceTypeRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);
}
