package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.implementation.support.api.shadow.type.AnnotationSupport;

public class AnnotationImpl extends DeclaredImpl implements R.Annotation
{
   public AnnotationImpl(Class<?> aClass)
   {
      super(aClass);
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
