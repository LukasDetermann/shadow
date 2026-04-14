package com.derivandi.api.adapter;

import com.derivandi.api.Ap;
import com.derivandi.internal.shadow.type.DeclaredImpl;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

public class DeclaredAdapter
{
   private final Ap.Declared declared;

   DeclaredAdapter(Ap.Declared declared)
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
