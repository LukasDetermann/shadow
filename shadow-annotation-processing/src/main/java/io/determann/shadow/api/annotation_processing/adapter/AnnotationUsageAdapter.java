package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.internal.annotation_processing.annotationvalue.AnnotationUsageImpl;

import javax.lang.model.element.AnnotationMirror;

public class AnnotationUsageAdapter
{
   private final AP.AnnotationUsage annotationUsage;

   AnnotationUsageAdapter(AP.AnnotationUsage annotationUsage)
   {
      this.annotationUsage = annotationUsage;
   }

   public AnnotationMirror toAnnotationMirror()
   {
      return ((AnnotationUsageImpl) annotationUsage).getAnnotationMirror();
   }
}