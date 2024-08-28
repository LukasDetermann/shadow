package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.ArrayRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.C_Array;

import static io.determann.shadow.api.Operations.ARRAY_GET_COMPONENT_TYPE;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class ArrayRendererImpl implements ArrayRenderer
{
   private final RenderingContextWrapper context;
   private final C_Array array;

   public ArrayRendererImpl(RenderingContext renderingContext, C_Array array)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.array = array;
   }

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
