package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.IntersectionRenderer;
import io.determann.shadow.api.shadow.Intersection;
import io.determann.shadow.impl.annotation_processing.ShadowApiImpl;

import java.util.stream.Collectors;

public class IntersectionRendererImpl implements IntersectionRenderer
{
   private final Context context;
   private final Intersection intersection;

   public IntersectionRendererImpl(Intersection intersection)
   {
      this.context = ((ShadowApiImpl) intersection.getApi()).getRenderingContext();
      this.intersection = intersection;
   }

   public static String type(Context context, Intersection intersection)
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
