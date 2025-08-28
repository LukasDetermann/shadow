package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.internal.annotation_processing.shadow.type.ArrayImpl;

import javax.lang.model.type.ArrayType;

public class ArrayAdapter
{
   private final AP.Array array;

   ArrayAdapter(AP.Array array)
   {
      this.array = array;
   }

   public ArrayType toArrayType()
   {
      return ((ArrayImpl) array).getMirror();
   }
}
