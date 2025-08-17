package io.determann.shadow.api.dsl.class_;

import org.jetbrains.annotations.Contract;

public interface ClassJavaDocStep
      extends ClassAnnotateStep
{
   @Contract(value = "_ -> new", pure = true)
   ClassAnnotateStep javadoc(String javadoc);
}