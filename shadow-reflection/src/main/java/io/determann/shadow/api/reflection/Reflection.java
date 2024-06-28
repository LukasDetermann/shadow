package io.determann.shadow.api.reflection;

import io.determann.shadow.api.shadow.type.Array;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.api.shadow.type.Primitive;

public class Reflection
{
   public static Array asArray(Array array)
   {
      return ReflectionAdapter.generalize(ReflectionAdapter.particularize(array).arrayType());
   }

   public static Array asArray(Primitive primitive)
   {
      return ReflectionAdapter.generalize(ReflectionAdapter.particularize(primitive).arrayType());
   }

   public static Array asArray(Declared declared)
   {
      return ReflectionAdapter.generalize(ReflectionAdapter.particularize(declared).arrayType());
   }
}