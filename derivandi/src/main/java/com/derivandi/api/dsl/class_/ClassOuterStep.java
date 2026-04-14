package com.derivandi.api.dsl.class_;

import com.derivandi.api.dsl.declared.DeclaredRenderable;

public interface ClassOuterStep
{
   ClassJavaDocStep outer(String qualifiedOuterType);

   ClassJavaDocStep outer(DeclaredRenderable outerType);
}