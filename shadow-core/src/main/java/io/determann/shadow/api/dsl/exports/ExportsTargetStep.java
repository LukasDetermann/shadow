package io.determann.shadow.api.dsl.exports;

import io.determann.shadow.api.shadow.structure.C_Module;

public interface ExportsTargetStep
      extends ExportsRenderable
{
   ExportsTargetStep to(String... moduleNames);

   ExportsTargetStep to(C_Module... modules);
}
