package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.Provider;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.renderer.WildcardRenderer;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.api.shadow.type.Wildcard;

import java.util.Optional;

import static io.determann.shadow.api.Operations.WILDCARD_GET_EXTENDS;
import static io.determann.shadow.api.Operations.WILDCARD_GET_SUPER;

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
      Optional<Shadow> wildcardExtends = Provider.requestOrEmpty(wildcard, WILDCARD_GET_EXTENDS);
      if (wildcardExtends.isPresent())
      {
         return "? extends " + ShadowRendererImpl.type(context, wildcardExtends.get());
      }

      Optional<Shadow> wildcardSuper = Provider.requestOrEmpty(wildcard, WILDCARD_GET_SUPER);
      if (wildcardSuper.isPresent())
      {
         return "? super " + ShadowRendererImpl.type(context, wildcardSuper.get());
      }
      return "?";
   }

   @Override
   public String type()
   {
      return type(context, wildcard);
   }
}
