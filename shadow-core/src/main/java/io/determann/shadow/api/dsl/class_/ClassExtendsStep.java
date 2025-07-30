package io.determann.shadow.api.dsl.class_;

public interface ClassExtendsStep extends ClassImplementsStep
{
   ClassImplementsStep extends_(String aClass);

   ClassImplementsStep extends_(ClassRenderable aClass);
}
