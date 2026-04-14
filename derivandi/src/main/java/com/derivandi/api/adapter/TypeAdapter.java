package com.derivandi.api.adapter;

import com.derivandi.api.Ap;
import com.derivandi.internal.shadow.type.TypeImpl;

import javax.lang.model.type.TypeMirror;

public class TypeAdapter
{
   private final Ap.Type type;

   TypeAdapter(Ap.Type type)
   {
      this.type = type;
   }

   public TypeMirror toTypeMirror()
   {
      //noinspection unchecked
      return ((TypeImpl<TypeMirror>) type).getMirror();
   }
}
