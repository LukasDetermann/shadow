package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.dsl.enum_constant.EnumConstantRenderable;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_Documented;

import static io.determann.shadow.api.Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES;
import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.enumConstant;
import static java.util.Collections.emptyList;

public interface C_EnumConstant
      extends C_Variable,
              C_Documented,
              EnumConstantRenderable
{
   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      return enumConstant()
            .annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
            .name(requestOrThrow(this, NAMEABLE_GET_NAME)).renderDeclaration(renderingContext);
   }
}
