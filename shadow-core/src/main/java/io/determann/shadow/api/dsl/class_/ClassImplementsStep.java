package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.interface_.InterfaceRenderable;

import java.util.Arrays;
import java.util.List;

public interface ClassImplementsStep
      extends ClassPermitsStep
{
   ClassImplementsStep implements_(String... interfaces);

   default ClassImplementsStep implements_(InterfaceRenderable... interfaces)
   {
      return implements_(Arrays.asList(interfaces));
   }

   ClassImplementsStep implements_(List<? extends InterfaceRenderable> interfaces);
}