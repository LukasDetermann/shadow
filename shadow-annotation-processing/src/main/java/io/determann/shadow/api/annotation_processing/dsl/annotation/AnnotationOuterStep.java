package io.determann.shadow.api.annotation_processing.dsl.annotation;

import io.determann.shadow.api.annotation_processing.dsl.declared.DeclaredRenderable;

public interface AnnotationOuterStep
{
   AnnotationJavaDocStep outer(String qualifiedOuterType);

   AnnotationJavaDocStep outer(DeclaredRenderable outerType);
}