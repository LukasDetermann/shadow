package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.renderer.ArrayRenderer;
import io.determann.shadow.api.shadow.Array;
import io.determann.shadow.impl.ShadowApiImpl;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class ArrayRendererImpl implements ArrayRenderer
{
   private final Context context;
   private final Array array;

   public ArrayRendererImpl(Array array)
   {
      this.context = ((ShadowApiImpl) array.getApi()).getRenderingContext();
      this.array = array;
   }

   public static String type(Context context, Array array)
   {
      return ShadowRendererImpl.type(context, array.getComponentType());
   }

   private static String renderDimensions(Array array)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("[]");
      while (array.getComponentType().isTypeKind(TypeKind.ARRAY))
      {
         sb.append("[]");
         array = ShadowApi.convert(array.getComponentType()).toArrayOrThrow();
      }
      return sb.toString();
   }

   @Override
   public String type()
   {
      return type(context, array) + renderDimensions(array);
   }

   @Override
   public String initialisation(int... dimensions)
   {
      return "new " +
             type(context, array) +
             stream(dimensions).mapToObj(value -> "[" + value + ']').collect(joining());
   }
}
