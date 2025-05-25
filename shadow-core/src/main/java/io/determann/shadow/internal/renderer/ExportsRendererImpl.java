package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.renderer.ExportsRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.directive.C_Exports;
import io.determann.shadow.api.shadow.structure.C_Module;

import static io.determann.shadow.api.Operations.EXPORTS_GET_PACKAGE;
import static io.determann.shadow.api.Operations.EXPORTS_GET_TARGET_MODULES;
import static io.determann.shadow.api.Provider.requestOrThrow;

public record ExportsRendererImpl(C_Exports exports)
      implements ExportsRenderer
{
   @Override
   public String declaration(RenderingContext renderingContext)
   {
      return Dsl.exports()
                .package_(requestOrThrow(exports, EXPORTS_GET_PACKAGE))
                .to(requestOrThrow(exports, EXPORTS_GET_TARGET_MODULES).toArray(C_Module[]::new))
                .render(renderingContext);
   }
}
