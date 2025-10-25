package io.determann.shadow.api.dsl.annotation;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;

public interface AnnotationOuterStep
{
   AnnotationJavaDocStep outer(String qualifiedOuterType);

   AnnotationJavaDocStep outer(DeclaredRenderable outerType);
}