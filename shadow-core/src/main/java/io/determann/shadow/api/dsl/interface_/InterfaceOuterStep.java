package io.determann.shadow.api.dsl.interface_;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;

public interface InterfaceOuterStep
{
   InterfaceJavaDocStep outer(String qualifiedOuterType);

   InterfaceJavaDocStep outer(DeclaredRenderable outerType);
}