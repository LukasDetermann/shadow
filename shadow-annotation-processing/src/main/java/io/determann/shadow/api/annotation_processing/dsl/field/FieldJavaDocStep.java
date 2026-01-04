package io.determann.shadow.api.annotation_processing.dsl.field;

import org.jetbrains.annotations.Contract;

public interface FieldJavaDocStep
      extends FieldAnnotateStep
{
   @Contract(value = "_ -> new", pure = true)
   FieldAnnotateStep javadoc(String javadoc);
}
