package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.IntersectionRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.C_Intersection;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.List;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.INTERSECTION_GET_BOUNDS;
import static io.determann.shadow.api.Provider.requestOrThrow;

public class IntersectionRendererImpl implements IntersectionRenderer
{
   private final C_Intersection intersection;

   public IntersectionRendererImpl(C_Intersection intersection)
   {
      this.intersection = intersection;
   }

   public static String type(RenderingContextWrapper context, C_Intersection intersection)
   {
      List<? extends C_Type> bounds = requestOrThrow(intersection, INTERSECTION_GET_BOUNDS);
      if (bounds.size() <= 1)
      {
         throw new IllegalStateException();
      }
      return bounds.stream().map(bound -> TypeRendererImpl.type(context, bound)).collect(Collectors.joining(" & "));
   }

   @Override
   public String declaration(RenderingContext renderingContext)
   {
      return type(new RenderingContextWrapper(renderingContext), intersection);
   }
}
