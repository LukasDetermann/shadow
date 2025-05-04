package io.determann.shadow.api.dsl.opens;

import io.determann.shadow.api.shadow.structure.C_Module;

public interface OpensTargetStep
      extends OpensRenderable
{
   OpensTargetStep to(String... moduleNames);

   OpensTargetStep to(C_Module... modules);
}
