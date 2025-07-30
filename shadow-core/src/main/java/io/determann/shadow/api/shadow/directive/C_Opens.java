package io.determann.shadow.api.shadow.directive;

import io.determann.shadow.api.dsl.opens.OpensRenderable;
import io.determann.shadow.api.renderer.RenderingContext;

import static io.determann.shadow.api.Operations.OPENS_GET_PACKAGE;
import static io.determann.shadow.api.Operations.OPENS_GET_TARGET_MODULES;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.opens;
import static java.util.Collections.emptyList;

/// Allows reflection access to a [Package][io.determann.shadow.api.Operations#OPENS_GET_PACKAGE]
/// for a [List\<Module\>][io.determann.shadow.api.Operations#OPENS_GET_TARGET_MODULES]
///  or to [All][io.determann.shadow.api.Operations#OPENS_TO_ALL]
public interface C_Opens
      extends C_Directive,
              OpensRenderable
{
   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      return opens().package_(requestOrThrow(this, OPENS_GET_PACKAGE))
                    .to(requestOrEmpty(this, OPENS_GET_TARGET_MODULES).orElse(emptyList()))
                    .renderDeclaration(renderingContext);
   }
}
