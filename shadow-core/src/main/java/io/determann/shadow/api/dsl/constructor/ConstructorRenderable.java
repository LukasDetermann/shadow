package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface ConstructorRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);
}
