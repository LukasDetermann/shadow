package com.derivandi.api.dsl.receiver;

import com.derivandi.api.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface ReceiverRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);
}
