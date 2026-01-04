package io.determann.shadow.api.annotation_processing.dsl.module;


import org.jetbrains.annotations.Contract;

public interface ModuleCopyrightHeaderStep
      extends ModuleJavaDocStep
{
   @Contract(value = "_ -> new", pure = true)
   ModuleJavaDocStep copyright(String copyrightHeader);
}
