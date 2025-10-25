package io.determann.shadow.api.dsl.enum_;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;

public interface EnumOuterStep
{
   EnumJavaDocStep outer(String qualifiedOuterType);

   EnumJavaDocStep outer(DeclaredRenderable outerType);
}