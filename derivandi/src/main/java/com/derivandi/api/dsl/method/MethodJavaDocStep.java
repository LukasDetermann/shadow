package com.derivandi.api.dsl.method;

import org.jetbrains.annotations.Contract;

public interface MethodJavaDocStep
      extends MethodAnnotateStep
{
   @Contract(value = "_ -> new", pure = true)
   MethodAnnotateStep javadoc(String javadoc);
}
