package io.determann.shadow.api.reflection.query;

import io.determann.shadow.api.Nameable;
import io.determann.shadow.api.shadow.Primitive;
import io.determann.shadow.api.shadow.Wildcard;

public interface ReflectionQueries
{
   public static NameableReflection query(Nameable nameable)
   {
      return ((NameableReflection) nameable);
   }

   public static WildcardReflection query(Wildcard wildcard)
   {
      return ((WildcardReflection) wildcard);
   }

   public static PrimitiveReflection query(Primitive primitive)
   {
      return ((PrimitiveReflection) primitive);
   }
}
