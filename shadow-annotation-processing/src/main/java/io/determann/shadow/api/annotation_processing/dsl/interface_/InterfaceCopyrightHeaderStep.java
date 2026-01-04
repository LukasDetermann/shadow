package io.determann.shadow.api.annotation_processing.dsl.interface_;


import org.jetbrains.annotations.Contract;

public interface InterfaceCopyrightHeaderStep
      extends InterfacePackageStep
{
   @Contract(value = "_ -> new", pure = true)
   InterfacePackageStep copyright(String copyrightHeader);
}
