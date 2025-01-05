package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.type.LM_Array;
import io.determann.shadow.internal.lang_model.shadow.type.ArrayImpl;

import javax.lang.model.type.ArrayType;

public class LM_ArrayAdapter
{
   private final LM_Array array;

   LM_ArrayAdapter(LM_Array array)
   {
      this.array = array;
   }

   public ArrayType toArrayType()
   {
      return ((ArrayImpl) array).getMirror();
   }
}
