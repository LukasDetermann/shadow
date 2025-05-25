package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.Operations;
import io.determann.shadow.api.Provider;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.renderer.UsesRenderer;
import io.determann.shadow.api.shadow.directive.C_Uses;

public record UsesRendererImpl(C_Uses uses)
      implements UsesRenderer
{
   @Override
   public String declaration(RenderingContext renderingContext)
   {
      return Dsl.uses()
                .service(Provider.requestOrThrow(uses, Operations.USES_GET_SERVICE))
                .render(renderingContext);
   }
}
