package io.determann.shadow.api.dsl.exports;

import io.determann.shadow.api.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface ExportsRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);
}
