package io.determann.shadow.api.annotation_processing.dsl.class_;

import io.determann.shadow.api.annotation_processing.dsl.declared.DeclaredRenderable;

public interface ClassOuterStep
{
   ClassJavaDocStep outer(String qualifiedOuterType);

   ClassJavaDocStep outer(DeclaredRenderable outerType);
}