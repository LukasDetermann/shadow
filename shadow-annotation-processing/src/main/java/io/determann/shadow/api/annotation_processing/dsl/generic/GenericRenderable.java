package io.determann.shadow.api.annotation_processing.dsl.generic;

import io.determann.shadow.api.annotation_processing.dsl.ReferenceTypeRenderable;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import org.jetbrains.annotations.Contract;

public interface GenericRenderable
      extends ReferenceTypeRenderable
{
   @Contract(value = "_ -> new", pure = true)
   String renderDeclaration(RenderingContext renderingContext);
}
