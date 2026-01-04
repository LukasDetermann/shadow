package io.determann.shadow.api.annotation_processing.dsl.enum_constant;

import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface EnumConstantRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);
}
