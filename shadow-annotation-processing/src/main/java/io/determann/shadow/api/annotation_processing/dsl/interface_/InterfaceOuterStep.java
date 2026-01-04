package io.determann.shadow.api.annotation_processing.dsl.interface_;

import io.determann.shadow.api.annotation_processing.dsl.declared.DeclaredRenderable;

public interface InterfaceOuterStep
{
   InterfaceJavaDocStep outer(String qualifiedOuterType);

   InterfaceJavaDocStep outer(DeclaredRenderable outerType);
}