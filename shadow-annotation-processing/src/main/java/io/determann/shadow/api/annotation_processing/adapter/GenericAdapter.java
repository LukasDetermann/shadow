package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.internal.annotation_processing.shadow.type.GenericImpl;

import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeVariable;

public class GenericAdapter
{
   private final AP.Generic generic;

   GenericAdapter(AP.Generic generic)
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
