package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.renderer.OpensRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.directive.C_Opens;
import io.determann.shadow.api.shadow.structure.C_Module;

import static io.determann.shadow.api.Operations.OPENS_GET_PACKAGE;
import static io.determann.shadow.api.Operations.OPENS_GET_TARGET_MODULES;
import static io.determann.shadow.api.Provider.requestOrThrow;

public record OpensRendererImpl(C_Opens opens) implements OpensRenderer
{
   @Override
   public String declaration(RenderingContext renderingContext)
   {
      return Dsl.opens()
                .package_(requestOrThrow(opens, OPENS_GET_PACKAGE))
                .to(requestOrThrow(opens, OPENS_GET_TARGET_MODULES).toArray(C_Module[]::new))
                .render(renderingContext);
   }
}
