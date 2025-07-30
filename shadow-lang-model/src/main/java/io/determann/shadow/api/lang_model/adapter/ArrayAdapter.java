package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.internal.lang_model.shadow.type.ArrayImpl;

import javax.lang.model.type.ArrayType;

public class ArrayAdapter
{
   private final LM.Array array;

   ArrayAdapter(LM.Array array)
   {
      this.array = array;
   }

   public ArrayType toArrayType()
   {
      return ((ArrayImpl) array).getMirror();
   }
}
