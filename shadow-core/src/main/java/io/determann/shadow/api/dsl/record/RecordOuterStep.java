package io.determann.shadow.api.dsl.record;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;

public interface RecordOuterStep
{
   RecordJavaDocStep outer(String qualifiedOuterType);

   RecordJavaDocStep outer(DeclaredRenderable outerType);
}