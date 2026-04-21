package com.derivandi.api.adapter;

import com.derivandi.api.D;
import com.derivandi.internal.annotationvalue.AnnotationUsageImpl;

import javax.lang.model.element.AnnotationMirror;

public class AnnotationUsageAdapter
{
   private final D.AnnotationUsage annotationUsage;

   AnnotationUsageAdapter(D.AnnotationUsage annotationUsage)
   {
      this.annotationUsage = annotationUsage;
   }

   public AnnotationMirror toAnnotationMirror()
   {
      return ((AnnotationUsageImpl) annotationUsage).getAnnotationMirror();
   }
}