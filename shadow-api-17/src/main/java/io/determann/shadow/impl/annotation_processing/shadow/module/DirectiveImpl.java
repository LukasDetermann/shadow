package io.determann.shadow.impl.annotation_processing.shadow.module;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.shadow.module.Directive;

public abstract class DirectiveImpl implements Directive
{
   private final AnnotationProcessingContext annotationProcessingContext;

   protected DirectiveImpl(AnnotationProcessingContext annotationProcessingContext)
   {
      this.annotationProcessingContext = annotationProcessingContext;
   }

   @Override
   public AnnotationProcessingContext getApi()
   {
      return annotationProcessingContext;
   }
}
