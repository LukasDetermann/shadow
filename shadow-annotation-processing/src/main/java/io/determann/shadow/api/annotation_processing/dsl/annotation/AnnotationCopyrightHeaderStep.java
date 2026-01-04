package io.determann.shadow.api.annotation_processing.dsl.annotation;


import org.jetbrains.annotations.Contract;

public interface AnnotationCopyrightHeaderStep
      extends AnnotationPackageStep
{
   @Contract(value = "_ -> new", pure = true)
   AnnotationPackageStep copyright(String copyrightHeader);
}
