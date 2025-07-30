package io.determann.shadow.api.shadow.directive;

import io.determann.shadow.api.dsl.exports.ExportsRenderable;
import io.determann.shadow.api.renderer.RenderingContext;

import static io.determann.shadow.api.Operations.EXPORTS_GET_PACKAGE;
import static io.determann.shadow.api.Operations.EXPORTS_GET_TARGET_MODULES;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.exports;
import static java.util.Collections.emptyList;

/// Exports a [Package][io.determann.shadow.api.Operations#GET_PACKAGE] to
/// a [List\<Module\>][io.determann.shadow.api.Operations#EXPORTS_GET_TARGET_MODULES]
/// or to [All][io.determann.shadow.api.Operations#EXPORTS_TO_ALL]
public interface C_Exports
      extends C_Directive,
              ExportsRenderable
{
   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      return exports().package_(requestOrThrow(this, EXPORTS_GET_PACKAGE))
                      .to(requestOrEmpty(this, EXPORTS_GET_TARGET_MODULES).orElse(emptyList()))
                      .renderDeclaration(renderingContext);
   }
}
