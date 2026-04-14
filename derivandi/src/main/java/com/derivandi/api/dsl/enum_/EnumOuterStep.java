package com.derivandi.api.dsl.enum_;

import com.derivandi.api.dsl.declared.DeclaredRenderable;

public interface EnumOuterStep
{
   EnumJavaDocStep outer(String qualifiedOuterType);

   EnumJavaDocStep outer(DeclaredRenderable outerType);
}