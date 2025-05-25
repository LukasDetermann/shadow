package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.ArrayRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.C_Array;

import static io.determann.shadow.api.Operations.ARRAY_GET_COMPONENT_TYPE;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public record ArrayRendererImpl(C_Array array) implements ArrayRenderer
{
   public static String type(RenderingContextWrapper context, C_Array array)
   {
      return TypeRendererImpl.type(context, requestOrThrow(array, ARRAY_GET_COMPONENT_TYPE));
   }

   private static String renderDimensions(C_Array array)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("[]");
      while (hasMoreDimensions(array))
      {
         sb.append("[]");
         array = ((C_Array) requestOrThrow(array, ARRAY_GET_COMPONENT_TYPE));
      }
      return sb.toString();
   }

   private static boolean hasMoreDimensions(C_Array array)
   {
      return requestOrThrow(array, ARRAY_GET_COMPONENT_TYPE) instanceof C_Array;
   }

   @Override
   public String type(RenderingContext renderingContext)
   {
      return type(new RenderingContextWrapper(renderingContext), array) + renderDimensions(array);
   }

   @Override
   public String initialisation(RenderingContext renderingContext, int... dimensions)
   {
      return "new " +
             type(new RenderingContextWrapper(renderingContext), array) +
             stream(dimensions).mapToObj(value -> "[" + value + ']').collect(joining());
   }
}
