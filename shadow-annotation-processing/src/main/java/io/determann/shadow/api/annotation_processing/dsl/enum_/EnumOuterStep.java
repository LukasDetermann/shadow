package io.determann.shadow.api.annotation_processing.dsl.enum_;

import io.determann.shadow.api.annotation_processing.dsl.declared.DeclaredRenderable;

public interface EnumOuterStep
{
   EnumJavaDocStep outer(String qualifiedOuterType);

   EnumJavaDocStep outer(DeclaredRenderable outerType);
}