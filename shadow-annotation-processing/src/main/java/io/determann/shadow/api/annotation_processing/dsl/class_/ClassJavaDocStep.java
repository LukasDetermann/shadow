package io.determann.shadow.api.annotation_processing.dsl.class_;

import org.jetbrains.annotations.Contract;

public interface ClassJavaDocStep
      extends ClassAnnotateStep
{
   @Contract(value = "_ -> new", pure = true)
   ClassAnnotateStep javadoc(String javadoc);
}