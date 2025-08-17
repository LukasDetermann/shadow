package io.determann.shadow.api.dsl.constructor;

import org.jetbrains.annotations.Contract;

public interface ConstructorJavaDocStep
      extends ConstructorAnnotateStep
{
   @Contract(value = "_ -> new", pure = true)
   ConstructorAnnotateStep javadoc(String javadoc);
}
