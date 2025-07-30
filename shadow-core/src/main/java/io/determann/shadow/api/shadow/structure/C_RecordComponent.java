package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.dsl.record_component.RecordComponentRenderable;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_Annotationable;
import io.determann.shadow.api.shadow.C_ModuleEnclosed;
import io.determann.shadow.api.shadow.C_Nameable;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.recordComponent;
import static java.util.Collections.emptyList;

public interface C_RecordComponent
      extends C_Nameable,
              C_Annotationable,
              C_ModuleEnclosed,
              RecordComponentRenderable
{
   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      return recordComponent().annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
                              .name(requestOrThrow(this, NAMEABLE_GET_NAME))
                              .type(requestOrThrow(this, RECORD_COMPONENT_GET_TYPE)).renderDeclaration(renderingContext);
   }
}
