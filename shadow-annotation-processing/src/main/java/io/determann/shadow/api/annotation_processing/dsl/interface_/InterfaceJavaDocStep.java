package io.determann.shadow.api.annotation_processing.dsl.interface_;

import org.jetbrains.annotations.Contract;

public interface InterfaceJavaDocStep
      extends InterfaceAnnotateStep
{
   @Contract(value = "_ -> new", pure = true)
   InterfaceAnnotateStep javadoc(String javadoc);
}