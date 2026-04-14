package com.derivandi.api.adapter;

import com.derivandi.api.Ap;
import com.derivandi.internal.shadow.type.GenericImpl;

import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeVariable;

public class GenericAdapter
{
   private final Ap.Generic generic;

   GenericAdapter(Ap.Generic generic)
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
