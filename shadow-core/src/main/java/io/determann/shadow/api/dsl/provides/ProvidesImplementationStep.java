package io.determann.shadow.api.dsl.provides;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;

import java.util.List;

public interface ProvidesImplementationStep
{
   ProvidesAdditionalImplementationStep with(String... implementationName);

   ProvidesAdditionalImplementationStep with(DeclaredRenderable... implementation);

   ProvidesAdditionalImplementationStep with(List<? extends DeclaredRenderable> implementation);
}
