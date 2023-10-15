package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.renderer.WildcardRenderer;
import io.determann.shadow.api.shadow.Wildcard;

public class WildcardRendererImpl implements WildcardRenderer
{
   private final RenderingContextWrapper context;
   private final Wildcard wildcard;

   public WildcardRendererImpl(RenderingContext renderingContext, Wildcard wildcard)
   {

      this.context = new RenderingContextWrapper(renderingContext);
      this.wildcard = wildcard;
   }

   static String type(RenderingContextWrapper context, Wildcard wildcard)
   {
      if (wildcard.getExtends().isPresent())
      {
         return "? extends " + ShadowRendererImpl.type(context, wildcard.getExtends().get());
      }
      if (wildcard.getSuper().isPresent())
      {
         return "? super " + ShadowRendererImpl.type(context, wildcard.getSuper().get());
      }
      return "?";
   }

   @Override
   public String type()
   {
      return type(context, wildcard);
   }
}
