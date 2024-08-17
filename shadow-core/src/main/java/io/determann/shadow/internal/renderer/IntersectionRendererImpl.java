package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.IntersectionRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.Intersection;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.List;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.INTERSECTION_GET_BOUNDS;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public class IntersectionRendererImpl implements IntersectionRenderer
{
   private final RenderingContextWrapper context;
   private final Intersection intersection;

   public IntersectionRendererImpl(RenderingContext renderingContext, Intersection intersection)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.intersection = intersection;
   }

   public static String type(RenderingContextWrapper context, Intersection intersection)
   {
      List<? extends Shadow> bounds = requestOrThrow(intersection, INTERSECTION_GET_BOUNDS);
      if (bounds.size() <= 1)
      {
         throw new IllegalStateException();
      }
      return bounds.stream().map(bound -> ShadowRendererImpl.type(context, bound)).collect(Collectors.joining(" & "));
   }

   @Override
   public String declaration()
   {
      return type(context, intersection);
   }
}
