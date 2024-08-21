package io.determann.shadow.api.reflection;

import io.determann.shadow.api.reflection.shadow.type.R_Array;
import io.determann.shadow.api.reflection.shadow.type.R_Declared;
import io.determann.shadow.api.reflection.shadow.type.R_Primitive;

public class Reflection
{
   public static R_Array asArray(R_Array array)
   {
      return R_Adapter.generalize(R_Adapter.particularize(array).arrayType());
   }

   public static R_Array asArray(R_Primitive primitive)
   {
      return R_Adapter.generalize(R_Adapter.particularize(primitive).arrayType());
   }

   public static R_Array asArray(R_Declared declared)
   {
      return R_Adapter.generalize(R_Adapter.particularize(declared).arrayType());
   }
}