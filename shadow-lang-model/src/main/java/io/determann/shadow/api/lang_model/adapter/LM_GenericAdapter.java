package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.type.LM_Generic;
import io.determann.shadow.internal.lang_model.shadow.type.GenericImpl;

import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeVariable;

public class LM_GenericAdapter
{
   private final LM_Generic generic;

   LM_GenericAdapter(LM_Generic generic)
   {
      this.generic = generic;
   }

   public TypeVariable toTypeVariable()
   {
      //noinspection OverlyStrongTypeCast
      return ((GenericImpl) generic).getMirror();
   }

   public TypeParameterElement toTypeParameterElement()
   {
      return ((GenericImpl) generic).getElement();
   }
}
