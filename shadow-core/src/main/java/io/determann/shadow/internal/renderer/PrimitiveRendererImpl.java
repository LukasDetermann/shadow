package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.PrimitiveRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;

public class PrimitiveRendererImpl implements PrimitiveRenderer
{
   private final RenderingContextWrapper context;
   private final C_Primitive primitive;

   public PrimitiveRendererImpl(RenderingContext renderingContext, C_Primitive primitive)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.primitive = primitive;
   }

   public static String type(RenderingContextWrapper context, C_Primitive primitive)
   {
      return requestOrThrow(primitive, NAMEABLE_GET_NAME);
   }

   @Override
   public String type()
   {
      return type(context, primitive);
   }
}
