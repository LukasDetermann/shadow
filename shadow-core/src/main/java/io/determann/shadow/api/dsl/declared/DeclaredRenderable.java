package io.determann.shadow.api.dsl.declared;

import io.determann.shadow.api.dsl.ReferenceTypeRenderable;
import io.determann.shadow.api.dsl.RenderingContext;
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
