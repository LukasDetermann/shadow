package io.determann.shadow.api.dsl.generic;

import io.determann.shadow.api.dsl.ReferenceTypeRenderable;
import io.determann.shadow.api.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface GenericRenderable
      extends ReferenceTypeRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);
}
