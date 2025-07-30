package io.determann.shadow.api.shadow.type.primitive;

import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_Nameable;
import io.determann.shadow.api.shadow.type.C_VariableType;

/**
 * represents primitive types, but not there wrapper classes. for example int, long, short
 */
public interface C_Primitive
      extends C_VariableType,
              C_Nameable
{
   @Override
   default String renderType(RenderingContext renderingContext)
   {
      return renderName(renderingContext);
   }
}
