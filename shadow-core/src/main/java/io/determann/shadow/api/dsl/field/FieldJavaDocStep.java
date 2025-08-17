package io.determann.shadow.api.dsl.field;

import org.jetbrains.annotations.Contract;

public interface FieldJavaDocStep
      extends FieldAnnotateStep
{
   @Contract(value = "_ -> new", pure = true)
   FieldAnnotateStep javadoc(String javadoc);
}
