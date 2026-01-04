package io.determann.shadow.api.annotation_processing.dsl.class_;


import org.jetbrains.annotations.Contract;

public interface ClassCopyrightHeaderStep
      extends ClassPackageStep
{
   @Contract(value = "_ -> new", pure = true)
   ClassPackageStep copyright(String copyrightHeader);
}
