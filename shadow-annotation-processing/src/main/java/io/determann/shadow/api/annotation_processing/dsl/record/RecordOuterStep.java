package io.determann.shadow.api.annotation_processing.dsl.record;

import io.determann.shadow.api.annotation_processing.dsl.declared.DeclaredRenderable;

public interface RecordOuterStep
{
   RecordJavaDocStep outer(String qualifiedOuterType);

   RecordJavaDocStep outer(DeclaredRenderable outerType);
}