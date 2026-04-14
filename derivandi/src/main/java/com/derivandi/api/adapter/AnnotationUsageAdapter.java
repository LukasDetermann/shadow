package com.derivandi.api.adapter;

import com.derivandi.api.Ap;
import com.derivandi.internal.annotationvalue.AnnotationUsageImpl;

import javax.lang.model.element.AnnotationMirror;

public class AnnotationUsageAdapter
{
   private final Ap.AnnotationUsage annotationUsage;

   AnnotationUsageAdapter(Ap.AnnotationUsage annotationUsage)
   {
      this.annotationUsage = annotationUsage;
   }

   public AnnotationMirror toAnnotationMirror()
   {
      return ((AnnotationUsageImpl) annotationUsage).getAnnotationMirror();
   }
}