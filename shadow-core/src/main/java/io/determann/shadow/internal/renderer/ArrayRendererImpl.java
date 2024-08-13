package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.ArrayRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.type.Array;

import static io.determann.shadow.api.shadow.Operations.ARRAY_GET_COMPONENT_TYPE;
import static io.determann.shadow.api.shadow.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class ArrayRendererImpl implements ArrayRenderer
{
   private final RenderingContextWrapper context;
   private final Array array;

   public ArrayRendererImpl(RenderingContext renderingContext, Array array)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.array = array;
   }

   public static String type(RenderingContextWrapper context, Array array)
   {
      return ShadowRendererImpl.type(context, requestOrThrow(array, ARRAY_GET_COMPONENT_TYPE));
   }

   private static String renderDimensions(Array array)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("[]");
      while (hasMoreDimensions(array))
      {
         sb.append("[]");
         array = ((Array) requestOrThrow(array, ARRAY_GET_COMPONENT_TYPE));
      }
      return sb.toString();
   }

   private static boolean hasMoreDimensions(Array array)
   {
      return requestOrThrow(requestOrThrow(array, ARRAY_GET_COMPONENT_TYPE), SHADOW_GET_KIND).equals(TypeKind.ARRAY);
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
