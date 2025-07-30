package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.Operations;
import io.determann.shadow.api.Provider;
import io.determann.shadow.api.dsl.array.ArrayRenderable;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_Erasable;

public interface C_Array
      extends C_ReferenceType,
              C_Erasable,
              ArrayRenderable
{
   @Override
   default String renderType(RenderingContext renderingContext)
   {
      return Provider.requestOrThrow(this, Operations.ARRAY_GET_COMPONENT_TYPE)
                     .renderName(renderingContext) + "[]";
   }

   @Override
   default String renderName(RenderingContext renderingContext)
   {
      return renderType(renderingContext);
   }
}