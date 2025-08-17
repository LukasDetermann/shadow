package io.determann.shadow.api.dsl.enum_;


import org.jetbrains.annotations.Contract;

public interface EnumCopyrightHeaderStep
      extends EnumPackageStep
{
   @Contract(value = "_ -> new", pure = true)
   EnumPackageStep copyright(String copyrightHeader);
}
