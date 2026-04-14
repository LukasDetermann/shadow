package com.derivandi.api.dsl.record;

import com.derivandi.api.dsl.declared.DeclaredRenderable;

public interface RecordOuterStep
{
   RecordJavaDocStep outer(String qualifiedOuterType);

   RecordJavaDocStep outer(DeclaredRenderable outerType);
}