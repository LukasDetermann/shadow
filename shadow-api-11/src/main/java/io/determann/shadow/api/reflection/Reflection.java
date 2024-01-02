package io.determann.shadow.api.reflection;

import io.determann.shadow.api.shadow.Array;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Primitive;

public class Reflection
{
   public static Array asArray(Array array)
   {
      return ReflectionAdapter.getShadow(java.lang.reflect.Array.newInstance(ReflectionAdapter.getReflection(array), 0).getClass());
   }

   public static Array asArray(Primitive primitive)
   {
      return ReflectionAdapter.getShadow(java.lang.reflect.Array.newInstance(ReflectionAdapter.getReflection(primitive), 0).getClass());
   }

   public static Array asArray(Declared declared)
   {
      return ReflectionAdapter.getShadow(java.lang.reflect.Array.newInstance(ReflectionAdapter.getReflection(declared), 0).getClass());
   }
}