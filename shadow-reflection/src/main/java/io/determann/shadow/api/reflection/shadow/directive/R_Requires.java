package io.determann.shadow.api.reflection.shadow.directive;

import io.determann.shadow.api.reflection.shadow.structure.R_Module;
import io.determann.shadow.api.shadow.directive.C_Requires;

/**
 * Dependency on another Module
 */
public non-sealed interface R_Requires extends R_Directive,
                                               C_Requires
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

   R_Module getDependency();
}
