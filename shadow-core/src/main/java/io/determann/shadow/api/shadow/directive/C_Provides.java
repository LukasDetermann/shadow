package io.determann.shadow.api.shadow.directive;

import io.determann.shadow.api.dsl.provides.ProvidesRenderable;
import io.determann.shadow.api.renderer.RenderingContext;

import static io.determann.shadow.api.Operations.PROVIDES_GET_IMPLEMENTATIONS;
import static io.determann.shadow.api.Operations.PROVIDES_GET_SERVICE;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.provides;
import static java.util.Collections.emptyList;

/// Provides a [Service][io.determann.shadow.api.Operations#PROVIDES_GET_SERVICE]
/// to other modules using [Implementations][io.determann.shadow.api.Operations#PROVIDES_GET_IMPLEMENTATIONS]
///
/// @see C_Uses
public interface C_Provides
      extends C_Directive,
              ProvidesRenderable
{
   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      return provides().service(requestOrThrow(this, PROVIDES_GET_SERVICE))
                       .with(requestOrEmpty(this, PROVIDES_GET_IMPLEMENTATIONS).orElse(emptyList()))
                       .renderDeclaration(renderingContext);
   }
}
