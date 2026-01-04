package io.determann.shadow.api.annotation_processing.dsl.provides;

import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface ProvidesRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);
}
