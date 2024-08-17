package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.type.AnnotationLangModel;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.AnnotationSupport;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

public class AnnotationImpl extends DeclaredImpl implements AnnotationLangModel
{
   public AnnotationImpl(LangModelContext context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public AnnotationImpl(LangModelContext context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return AnnotationSupport.representsSameType(this, shadow);
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
