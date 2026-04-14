package com.derivandi.api.dsl.interface_;

import com.derivandi.api.dsl.declared.DeclaredRenderable;

public interface InterfaceOuterStep
{
   InterfaceJavaDocStep outer(String qualifiedOuterType);

   InterfaceJavaDocStep outer(DeclaredRenderable outerType);
}