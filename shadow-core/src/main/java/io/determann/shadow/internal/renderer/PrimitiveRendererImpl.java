package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.PrimitiveRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.renderer.RenderingContextWrapper.wrap;

public record PrimitiveRendererImpl(C_Primitive primitive) implements PrimitiveRenderer
{
   public static String type(RenderingContextWrapper context, C_Primitive primitive)
   {
      return requestOrThrow(primitive, NAMEABLE_GET_NAME);
   }

   @Override
   public String type(RenderingContext renderingContext)
   {
      return type(wrap(renderingContext), primitive);
   }
}
