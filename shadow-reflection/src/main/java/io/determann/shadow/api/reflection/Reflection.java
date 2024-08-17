package io.determann.shadow.api.reflection;

import io.determann.shadow.api.reflection.shadow.type.ArrayReflection;
import io.determann.shadow.api.reflection.shadow.type.DeclaredReflection;
import io.determann.shadow.api.reflection.shadow.type.PrimitiveReflection;

public class Reflection
{
   public static ArrayReflection asArray(ArrayReflection array)
   {
      return ReflectionAdapter.generalize(ReflectionAdapter.particularize(array).arrayType());
   }

   public static ArrayReflection asArray(PrimitiveReflection primitive)
   {
      return ReflectionAdapter.generalize(ReflectionAdapter.particularize(primitive).arrayType());
   }

   public static ArrayReflection asArray(DeclaredReflection declared)
   {
      return ReflectionAdapter.generalize(ReflectionAdapter.particularize(declared).arrayType());
   }
}