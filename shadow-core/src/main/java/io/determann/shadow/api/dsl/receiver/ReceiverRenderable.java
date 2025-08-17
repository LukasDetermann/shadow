package io.determann.shadow.api.dsl.receiver;

import io.determann.shadow.api.renderer.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface ReceiverRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);
}
