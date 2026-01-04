package io.determann.shadow.api.annotation_processing.dsl.enum_constant;

import org.jetbrains.annotations.Contract;

public interface EnumConstantJavaDocStep
      extends EnumConstantAnnotateStep
{
   @Contract(value = "_ -> new", pure = true)
   EnumConstantAnnotateStep javadoc(String javadoc);
}
