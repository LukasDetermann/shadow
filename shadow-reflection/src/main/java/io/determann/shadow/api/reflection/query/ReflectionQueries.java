package io.determann.shadow.api.reflection.query;

import io.determann.shadow.api.Nameable;

public interface ReflectionQueries
{
   public static NameableReflection query(Nameable nameable)
   {
      return ((NameableReflection) nameable);
   }
}
