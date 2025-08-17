package io.determann.shadow.api.dsl.class_;


import org.jetbrains.annotations.Contract;

public interface ClassCopyrightHeaderStep
      extends ClassPackageStep
{
   @Contract(value = "_ -> new", pure = true)
   ClassPackageStep copyright(String copyrightHeader);
}
