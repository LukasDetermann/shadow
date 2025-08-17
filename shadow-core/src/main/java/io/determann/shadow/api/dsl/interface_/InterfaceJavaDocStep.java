package io.determann.shadow.api.dsl.interface_;

import org.jetbrains.annotations.Contract;

public interface InterfaceJavaDocStep
      extends InterfaceAnnotateStep
{
   @Contract(value = "_ -> new", pure = true)
   InterfaceAnnotateStep javadoc(String javadoc);
}