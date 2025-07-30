package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.renderer.RenderingContext;

public interface C_Void
      extends C_Type
{
   @Override
   default String renderName(RenderingContext renderingContext)
   {
      return "void";
   }
}