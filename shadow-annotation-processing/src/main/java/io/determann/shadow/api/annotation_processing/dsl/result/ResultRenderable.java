package io.determann.shadow.api.annotation_processing.dsl.result;

import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface ResultRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);
}
