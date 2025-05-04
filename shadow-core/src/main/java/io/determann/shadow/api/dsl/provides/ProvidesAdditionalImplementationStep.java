package io.determann.shadow.api.dsl.provides;

import io.determann.shadow.api.shadow.type.C_Declared;

public interface ProvidesAdditionalImplementationStep
      extends ProvidesRenderable
{
   ProvidesAdditionalImplementationStep with(String... implementationName);

   ProvidesAdditionalImplementationStep with(C_Declared... implementation);
}
