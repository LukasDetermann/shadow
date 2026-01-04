package io.determann.shadow.api.annotation_processing.dsl.package_;

import org.jetbrains.annotations.Contract;

public interface PackageJavaDocStep
      extends PackageAnnotateStep
{
   @Contract(value = "_ -> new", pure = true)
   PackageAnnotateStep javadoc(String javadoc);
}
