package io.determann.shadow.api.annotation_processing.dsl.enum_;

import org.jetbrains.annotations.Contract;

public interface EnumJavaDocStep
      extends EnumAnnotateStep
{
   @Contract(value = "_ -> new", pure = true)
   EnumAnnotateStep javadoc(String javadoc);
}