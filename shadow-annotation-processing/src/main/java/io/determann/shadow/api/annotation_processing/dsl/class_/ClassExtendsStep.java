package io.determann.shadow.api.annotation_processing.dsl.class_;

import org.jetbrains.annotations.Contract;

public interface ClassExtendsStep
      extends ClassImplementsStep
{
   @Contract(value = "_ -> new", pure = true)
   ClassImplementsStep extends_(String aClass);

   @Contract(value = "_ -> new", pure = true)
   ClassImplementsStep extends_(ClassRenderable aClass);
}
