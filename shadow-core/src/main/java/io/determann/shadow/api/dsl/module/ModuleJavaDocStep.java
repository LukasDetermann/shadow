package io.determann.shadow.api.dsl.module;

import org.jetbrains.annotations.Contract;

public interface ModuleJavaDocStep
      extends ModuleAnnotateStep
{
   @Contract(value = "_ -> new", pure = true)
   ModuleAnnotateStep javadoc(String javadoc);
}
