package io.determann.shadow.api.shadow.directive;

import io.determann.shadow.api.dsl.requires.RequiresNameStep;
import io.determann.shadow.api.dsl.requires.RequiresRenderable;
import io.determann.shadow.api.renderer.RenderingContext;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.requires;

/**
 * Dependency on another Module
 */
public interface C_Requires
      extends C_Directive,
              RequiresRenderable
{
   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      RequiresNameStep nameStep;
      if (requestOrEmpty(this, REQUIRES_IS_STATIC).orElse(false))
      {
         nameStep = requires().static_();
      }
      else if (requestOrEmpty(this, REQUIRES_IS_TRANSITIVE).orElse(false))
      {
         nameStep = requires().transitive();
      }
      else
      {
         nameStep = requires();
      }
      return nameStep.dependency(requestOrThrow(this, REQUIRES_GET_DEPENDENCY))
                     .renderDeclaration(renderingContext);
   }
}