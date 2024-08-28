package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.type.R_Annotation;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.implementation.support.api.shadow.type.AnnotationSupport;

public class AnnotationImpl extends DeclaredImpl implements R_Annotation
{
   public AnnotationImpl(Class<?> aClass)
   {
      super(aClass);
   }

   @Override
   public boolean representsSameType(C_Type type)
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
