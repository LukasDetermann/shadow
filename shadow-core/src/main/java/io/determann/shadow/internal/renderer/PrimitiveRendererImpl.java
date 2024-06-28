package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.PrimitiveRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.Primitive;

import static io.determann.shadow.api.shadow.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public class PrimitiveRendererImpl implements PrimitiveRenderer
{
   private final RenderingContextWrapper context;
   private final Primitive primitive;

   public PrimitiveRendererImpl(RenderingContext renderingContext, Primitive primitive)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.primitive = primitive;
   }

   public static String type(RenderingContextWrapper context, Primitive primitive)
   {
      return switch (requestOrThrow(primitive, SHADOW_GET_KIND))
      {
         case BOOLEAN -> "boolean";
         case BYTE -> "byte";
         case SHORT -> "short";
         case INT -> "int";
         case LONG -> "long";
         case CHAR -> "char";
         case FLOAT -> "float";
         case DOUBLE -> "double";
         default -> throw new IllegalStateException();
      };
   }

   @Override
   public String type()
   {
      return type(context, primitive);
   }
}
