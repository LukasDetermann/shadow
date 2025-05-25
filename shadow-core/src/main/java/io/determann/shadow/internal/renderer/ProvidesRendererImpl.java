package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.renderer.ProvidesRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.directive.C_Provides;
import io.determann.shadow.api.shadow.type.C_Declared;

import static io.determann.shadow.api.Operations.PROVIDES_GET_IMPLEMENTATIONS;
import static io.determann.shadow.api.Operations.PROVIDES_GET_SERVICE;
import static io.determann.shadow.api.Provider.requestOrThrow;

public record ProvidesRendererImpl(C_Provides provides) implements ProvidesRenderer
{
   @Override
   public String declaration(RenderingContext renderingContext)
   {
      return Dsl.provides()
                .service(requestOrThrow(provides, PROVIDES_GET_SERVICE))
                .with(requestOrThrow(provides, PROVIDES_GET_IMPLEMENTATIONS).toArray(C_Declared[]::new))
                .render(renderingContext);
   }
}
