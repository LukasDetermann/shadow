package io.determann.shadow.api.dsl.package_;

import org.jetbrains.annotations.Contract;

public interface PackageJavaDocStep
      extends PackageAnnotateStep
{
   @Contract(value = "_ -> new", pure = true)
   PackageAnnotateStep javadoc(String javadoc);
}
