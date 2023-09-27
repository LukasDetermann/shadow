package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.WildcardRenderer;
import io.determann.shadow.api.shadow.Wildcard;
import io.determann.shadow.impl.ShadowApiImpl;

public class WildcardRendererImpl implements WildcardRenderer
{
   private final Context context;
   private final Wildcard wildcard;

   public WildcardRendererImpl(Wildcard wildcard)
   {

      this.context = ((ShadowApiImpl) wildcard.getApi()).getRenderingContext();
      this.wildcard = wildcard;
   }

   static String type(Context context, Wildcard wildcard)
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
