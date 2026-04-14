package com.derivandi.api.dsl.declared;

import com.derivandi.api.dsl.ReferenceTypeRenderable;
import com.derivandi.api.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface DeclaredRenderable
      extends ReferenceTypeRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);

   @Contract(value = "_ -> new", pure = true)
   String renderQualifiedName(RenderingContext renderingContext);

   @Contract(value = "_ -> new", pure = true)
   String renderSimpleName(RenderingContext renderingContext);
}
