package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.implementation.support.api.shadow.type.AnnotationSupport;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

public class AnnotationImpl extends DeclaredImpl implements AP.Annotation
{
   public AnnotationImpl(AP.Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public AnnotationImpl(AP.Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public boolean representsSameType(C.Type type)
   {
      return AnnotationSupport.representsSameType(this, type);
   }

   @Override
   public boolean equals(Object other)
   {
      return AnnotationSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return AnnotationSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return AnnotationSupport.toString(this);
   }
}
