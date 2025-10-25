package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;

public interface ClassOuterStep
{
   ClassJavaDocStep outer(String qualifiedOuterType);

   ClassJavaDocStep outer(DeclaredRenderable outerType);
}