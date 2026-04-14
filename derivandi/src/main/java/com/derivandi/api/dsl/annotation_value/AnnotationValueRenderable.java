package com.derivandi.api.dsl.annotation_value;

import com.derivandi.api.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface AnnotationValueRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String render(RenderingContext renderingContext);
}
