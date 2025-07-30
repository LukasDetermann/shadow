package io.determann.shadow.api.dsl.provides;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;

import java.util.Arrays;
import java.util.List;

public interface ProvidesAdditionalImplementationStep
      extends ProvidesRenderable,
              ProvidesImplementationStep
{
   ProvidesAdditionalImplementationStep with(String... implementationName);

   default ProvidesAdditionalImplementationStep with(DeclaredRenderable... implementation)
   {
      return with(Arrays.asList(implementation));
   }

   ProvidesAdditionalImplementationStep with(List<? extends DeclaredRenderable> implementation);
}