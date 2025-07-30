package io.determann.shadow.api.dsl.enum_;

import io.determann.shadow.api.dsl.interface_.InterfaceRenderable;

import java.util.Arrays;
import java.util.List;

public interface EnumImplementsStep
      extends EnumEnumConstantStep
{
   EnumImplementsStep implements_(String... interfaces);

   default EnumImplementsStep implements_(InterfaceRenderable... interfaces)
   {
      return implements_(Arrays.asList(interfaces));
   }

   EnumImplementsStep implements_(List<? extends InterfaceRenderable> interfaces);
}