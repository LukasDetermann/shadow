package io.determann.shadow.api.shadow.directive;

import io.determann.shadow.api.dsl.uses.UsesRenderable;
import io.determann.shadow.api.renderer.RenderingContext;

import static io.determann.shadow.api.Operations.USES_GET_SERVICE;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.uses;

/**
 * Uses a Service of another module
 *
 * @see C_Provides
 */
public interface C_Uses
      extends C_Directive,
              UsesRenderable
{
   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      return uses(requestOrThrow(this, USES_GET_SERVICE)).renderDeclaration(renderingContext);
   }
}