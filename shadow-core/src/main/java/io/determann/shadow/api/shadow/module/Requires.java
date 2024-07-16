package io.determann.shadow.api.shadow.module;

import io.determann.shadow.api.shadow.structure.Module;

/**
 * Dependency on another Module
 */
public non-sealed interface Requires extends Directive
{
   /**
    * The dependent module is required at compile time and optional at runtime
    */
   boolean isStatic();

   /**
    * Marks transitive Dependencies.
    * Let's say A depends on B
    * and B depends transitive on C.
    * In this case A also depends on C. This is needed when B exposes C
    */
   boolean isTransitive();

   Module getDependency();
}
