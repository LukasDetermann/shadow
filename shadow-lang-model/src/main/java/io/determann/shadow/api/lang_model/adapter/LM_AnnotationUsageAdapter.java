package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.LM_AnnotationUsage;
import io.determann.shadow.internal.lang_model.annotationvalue.AnnotationUsageImpl;

import javax.lang.model.element.AnnotationMirror;

public class LM_AnnotationUsageAdapter
{
   private final LM_AnnotationUsage annotationUsage;

   LM_AnnotationUsageAdapter(LM_AnnotationUsage annotationUsage)
   {
      this.annotationUsage = annotationUsage;
   }

   public AnnotationMirror toAnnotationMirror()
   {
      return ((AnnotationUsageImpl) annotationUsage).getAnnotationMirror();
   }
}