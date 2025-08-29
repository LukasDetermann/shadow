package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.internal.annotation_processing.shadow.type.DeclaredImpl;

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
