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
      switch (primitive.getTypeKind())
      {
         case BOOLEAN:
            return "boolean";
         case BYTE:
            return "byte";
         case SHORT:
            return "short";
         case INT:
            return "int";
         case LONG:
            return "long";
         case CHAR:
            return "char";
         case FLOAT:
            return "float";
         case DOUBLE:
            return "double";
         default:
            throw new IllegalStateException();
      }
   }

   @Override
   public String type()
   {
      return type(context, primitive);
   }
}
