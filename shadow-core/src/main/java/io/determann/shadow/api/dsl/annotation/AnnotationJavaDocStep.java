package io.determann.shadow.api.dsl.annotation;

import org.jetbrains.annotations.Contract;

public interface AnnotationJavaDocStep
      extends AnnotationAnnotateStep
{
   @Contract(value = "_ -> new", pure = true)
   AnnotationAnnotateStep javadoc(String javadoc);
}
