package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.internal.annotation_processing.annotationvalue.AnnotationUsageImpl;

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