package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.internal.annotation_processing.shadow.type.TypeImpl;

import javax.lang.model.type.TypeMirror;

public class TypeAdapter
{
   private final AP.Type type;

   TypeAdapter(AP.Type type)
   {
      this.type = type;
   }

   public TypeMirror toTypeMirror()
   {
      //noinspection unchecked
      return ((TypeImpl<TypeMirror>) type).getMirror();
   }
}
