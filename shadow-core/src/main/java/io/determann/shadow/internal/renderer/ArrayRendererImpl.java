package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.renderer.ArrayRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.Array;

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
      return ShadowRendererImpl.type(context, array.getComponentType());
   }

   private static String renderDimensions(Array array)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("[]");
      while (array.getComponentType().isKind(TypeKind.ARRAY))
      {
         sb.append("[]");
         array = Converter.convert(array.getComponentType()).toArrayOrThrow();
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
