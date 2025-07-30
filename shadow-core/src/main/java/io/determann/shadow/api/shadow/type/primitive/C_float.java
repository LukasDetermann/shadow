package io.determann.shadow.api.shadow.type.primitive;

import io.determann.shadow.api.renderer.RenderingContext;

public interface C_float
      extends C_Primitive
{
   @Override
   default String renderName(RenderingContext renderingContext)
   {
      return "float";
   }
}