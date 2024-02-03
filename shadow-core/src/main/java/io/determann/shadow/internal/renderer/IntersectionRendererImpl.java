package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.IntersectionRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.Intersection;

import java.util.stream.Collectors;

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
      if (intersection.getBounds().size() <= 1)
      {
         throw new IllegalStateException();
      }
      return intersection.getBounds().stream().map(bound -> ShadowRendererImpl.type(context, bound)).collect(Collectors.joining(" & "));
   }

   @Override
   public String declaration()
   {
      return type(context, intersection);
   }
}
