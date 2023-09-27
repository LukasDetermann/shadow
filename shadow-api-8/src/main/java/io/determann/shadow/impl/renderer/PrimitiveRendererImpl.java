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
