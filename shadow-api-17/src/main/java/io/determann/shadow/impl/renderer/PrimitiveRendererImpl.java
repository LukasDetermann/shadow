package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.PrimitiveRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.Primitive;

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
      return switch (primitive.getTypeKind())
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
