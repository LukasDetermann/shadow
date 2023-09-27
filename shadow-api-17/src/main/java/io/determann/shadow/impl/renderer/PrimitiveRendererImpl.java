package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.PrimitiveRenderer;
import io.determann.shadow.api.shadow.Primitive;
import io.determann.shadow.impl.ShadowApiImpl;

public class PrimitiveRendererImpl implements PrimitiveRenderer
{
   private final Context context;
   private final Primitive primitive;

   public PrimitiveRendererImpl(Primitive primitive)
   {
      this.context = ((ShadowApiImpl) primitive.getApi()).getRenderingContext();
      this.primitive = primitive;
   }

   public static String type(Context context, Primitive primitive)
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
