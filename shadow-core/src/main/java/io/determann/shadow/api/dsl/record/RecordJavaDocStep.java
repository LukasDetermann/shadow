package io.determann.shadow.api.dsl.record;

import org.jetbrains.annotations.Contract;

public interface RecordJavaDocStep
      extends RecordAnnotateStep
{
   @Contract(value = "_ -> new", pure = true)
   RecordAnnotateStep javadoc(String javadoc);
}