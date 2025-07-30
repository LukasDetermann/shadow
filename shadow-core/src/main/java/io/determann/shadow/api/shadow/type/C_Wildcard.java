package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_Erasable;

import java.util.Optional;

import static io.determann.shadow.api.Operations.WILDCARD_GET_EXTENDS;
import static io.determann.shadow.api.Operations.WILDCARD_GET_SUPER;
import static io.determann.shadow.api.Provider.requestOrEmpty;

/**
 * {@snippet id = "test":
 *  List<? extends Number>//@highlight substring="? extends Number"
 *}
 * or
 * {@snippet :
 *  List<? super Number>//@highlight substring="? super Number"
 *}
 */
public interface C_Wildcard
      extends C_Type,
              C_Erasable
{
   @Override
   default String renderName(RenderingContext renderingContext)
   {
      Optional<C_Type> superType = requestOrEmpty(this, WILDCARD_GET_SUPER);
      if (superType.isPresent())
      {
         return "? super " + superType.get().renderName(renderingContext);
      }
      Optional<C_Type> extendsType = requestOrEmpty(this, WILDCARD_GET_EXTENDS);
      if (extendsType.isPresent())
      {
         return "? extends " + extendsType.get();
      }
      return "?";
   }
}