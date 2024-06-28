package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.reflection.shadow.type.ShadowReflection;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.type.Null;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.Objects;

import static io.determann.shadow.api.shadow.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class NullImpl implements Null,
                                 ShadowReflection
{
   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null && TypeKind.NULL.equals(requestOrThrow(shadow, SHADOW_GET_KIND));
   }

   @Override
   public TypeKind getKind()
   {
      return TypeKind.NULL;
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(getKind());
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Null;
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
