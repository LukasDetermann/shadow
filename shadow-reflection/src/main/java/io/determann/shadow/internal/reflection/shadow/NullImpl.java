package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.reflection.query.ShadowReflection;
import io.determann.shadow.api.shadow.Null;
import io.determann.shadow.api.shadow.Shadow;

import java.util.Objects;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;
import static io.determann.shadow.meta_meta.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

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
