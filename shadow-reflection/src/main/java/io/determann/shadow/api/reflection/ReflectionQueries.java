package io.determann.shadow.api.reflection;

import io.determann.shadow.api.Nameable;
import io.determann.shadow.api.reflection.query.NameableReflection;
import io.determann.shadow.api.reflection.query.PrimitiveReflection;
import io.determann.shadow.api.reflection.query.ShadowReflection;
import io.determann.shadow.api.reflection.query.WildcardReflection;
import io.determann.shadow.api.shadow.Primitive;
import io.determann.shadow.api.shadow.Shadow;
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

   public static ShadowReflection query(Shadow shadow)
   {
      return ((ShadowReflection) shadow);
   }
}
