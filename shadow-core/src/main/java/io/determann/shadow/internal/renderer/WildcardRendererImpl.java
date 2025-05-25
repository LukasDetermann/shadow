package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.Provider;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.renderer.WildcardRenderer;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.api.shadow.type.C_Wildcard;

import java.util.Optional;

import static io.determann.shadow.api.Operations.WILDCARD_GET_EXTENDS;
import static io.determann.shadow.api.Operations.WILDCARD_GET_SUPER;

public record WildcardRendererImpl(C_Wildcard wildcard) implements WildcardRenderer
{
   static String type(RenderingContextWrapper context, C_Wildcard wildcard)
   {
      Optional<C_Type> wildcardExtends = Provider.requestOrEmpty(wildcard, WILDCARD_GET_EXTENDS);
      if (wildcardExtends.isPresent())
      {
         return "? extends " + TypeRendererImpl.type(context, wildcardExtends.get());
      }

      Optional<C_Type> wildcardSuper = Provider.requestOrEmpty(wildcard, WILDCARD_GET_SUPER);
      return wildcardSuper.map(type -> "? super " + TypeRendererImpl.type(context, type)).orElse("?");
   }

   @Override
   public String type(RenderingContext renderingContext)
   {
      return type(new RenderingContextWrapper(renderingContext), wildcard);
   }
}
