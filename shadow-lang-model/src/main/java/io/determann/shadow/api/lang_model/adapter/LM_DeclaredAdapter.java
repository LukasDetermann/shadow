package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.internal.lang_model.shadow.type.DeclaredImpl;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

public class LM_DeclaredAdapter
{
   private final LM_Declared declared;

   LM_DeclaredAdapter(LM_Declared declared)
   {
      this.declared = declared;
   }

   public DeclaredType toDeclaredType()
   {
      //noinspection OverlyStrongTypeCast
      return ((DeclaredImpl) declared).getMirror();
   }

   public TypeElement toTypeElement()
   {
      return ((DeclaredImpl) declared).getElement();
   }
}
