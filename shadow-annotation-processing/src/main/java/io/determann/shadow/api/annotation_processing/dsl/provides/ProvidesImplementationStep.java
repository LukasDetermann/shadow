package io.determann.shadow.api.annotation_processing.dsl.provides;

import io.determann.shadow.api.annotation_processing.dsl.declared.DeclaredRenderable;
import org.jetbrains.annotations.Contract;

import java.util.List;

public interface ProvidesImplementationStep
{
   @Contract(value = "_ -> new", pure = true)
   ProvidesAdditionalImplementationStep with(String... implementationName);

   @Contract(value = "_ -> new", pure = true)
   ProvidesAdditionalImplementationStep with(DeclaredRenderable... implementation);

   @Contract(value = "_ -> new", pure = true)
   ProvidesAdditionalImplementationStep with(List<? extends DeclaredRenderable> implementation);
}
