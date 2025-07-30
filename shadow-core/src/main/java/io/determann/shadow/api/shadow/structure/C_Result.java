package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.dsl.result.ResultRenderable;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_Annotationable;

import static io.determann.shadow.api.Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES;
import static io.determann.shadow.api.Operations.RESULT_GET_TYPE;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.result;
import static java.util.Collections.emptyList;

public interface C_Result
      extends C_Annotationable,
              ResultRenderable
{
   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      return result().annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
                     .type(requestOrThrow(this, RESULT_GET_TYPE)).renderDeclaration(renderingContext);
   }
}
