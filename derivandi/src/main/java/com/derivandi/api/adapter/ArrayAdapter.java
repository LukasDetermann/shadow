package com.derivandi.api.adapter;

import com.derivandi.api.Ap;
import com.derivandi.internal.shadow.type.ArrayImpl;

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
