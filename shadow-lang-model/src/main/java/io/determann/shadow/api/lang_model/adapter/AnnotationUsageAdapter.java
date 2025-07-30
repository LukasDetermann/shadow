package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.internal.lang_model.annotationvalue.AnnotationUsageImpl;

import javax.lang.model.element.AnnotationMirror;

public class AnnotationUsageAdapter
{
   private final LM.AnnotationUsage annotationUsage;

   AnnotationUsageAdapter(LM.AnnotationUsage annotationUsage)
   {
      this.annotationUsage = annotationUsage;
   }

   public AnnotationMirror toAnnotationMirror()
   {
      return ((AnnotationUsageImpl) annotationUsage).getAnnotationMirror();
   }
}