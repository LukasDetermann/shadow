package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.requires.RequiresModifierStep;
import io.determann.shadow.api.dsl.requires.RequiresNameStep;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.renderer.RequiresRenderer;
import io.determann.shadow.api.shadow.directive.C_Requires;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;

public record RequiresRendererImpl(C_Requires requires)
      implements RequiresRenderer
{
   @Override
   public String declaration(RenderingContext renderingContext)
   {
      RequiresModifierStep requiresModifierStep = Dsl.requires();

      RequiresNameStep requiresNameStep;
      if (requestOrEmpty(requires, REQUIRES_IS_STATIC).orElse(false))
      {
         requiresNameStep = requiresModifierStep.static_();
      }
      else if (requestOrEmpty(requires, REQUIRES_IS_TRANSITIVE).orElse(false))
      {
         requiresNameStep = requiresModifierStep.transitive();
      }
      else
      {
         requiresNameStep = requiresModifierStep;
      }

      return requiresNameStep.dependency(requestOrThrow(requires, REQUIRES_GET_DEPENDENCY))
                             .render(renderingContext);
   }
}
