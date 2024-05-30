package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.reflection.query.ShadowReflection;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.api.shadow.Void;

import java.util.Objects;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;
import static io.determann.shadow.meta_meta.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

public class VoidImpl implements Void,
                                 ShadowReflection
{
   @Override
   public TypeKind getKind()
   {
      return TypeKind.VOID;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null && TypeKind.VOID.equals(requestOrThrow(shadow, SHADOW_GET_KIND));
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(getKind());
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Void;
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
