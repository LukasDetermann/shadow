package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.internal.lang_model.shadow.type.TypeImpl;

import javax.lang.model.type.TypeMirror;

public class TypeAdapter
{
   private final LM.Type type;

   TypeAdapter(LM.Type type)
   {
      this.type = type;
   }

   public TypeMirror toTypeMirror()
   {
      //noinspection unchecked
      return ((TypeImpl<TypeMirror>) type).getMirror();
   }
}
