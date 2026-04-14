package com.derivandi.api.dsl.annotation;

import com.derivandi.api.dsl.declared.DeclaredRenderable;

public interface AnnotationOuterStep
{
   AnnotationJavaDocStep outer(String qualifiedOuterType);

   AnnotationJavaDocStep outer(DeclaredRenderable outerType);
}