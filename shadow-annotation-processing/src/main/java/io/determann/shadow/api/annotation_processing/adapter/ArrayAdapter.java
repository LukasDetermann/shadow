package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.internal.annotation_processing.shadow.type.ArrayImpl;

import javax.lang.model.type.ArrayType;

public class ArrayAdapter
{
   private final Ap.Array array;

   ArrayAdapter(Ap.Array array)
   {
      this.array = array;
   }

   public ArrayType toArrayType()
   {
      //noinspection OverlyStrongTypeCast
      return ((ArrayImpl) array).getMirror();
   }
}
