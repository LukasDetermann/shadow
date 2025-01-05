package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.internal.lang_model.shadow.type.TypeImpl;

import javax.lang.model.type.TypeMirror;

public class LM_TypeAdapter
{
   private final LM_Type type;

   LM_TypeAdapter(LM_Type type)
   {
      this.type = type;
   }

   public TypeMirror toTypeMirror()
   {
      //noinspection unchecked
      return ((TypeImpl<TypeMirror>) type).getMirror();
   }
}
