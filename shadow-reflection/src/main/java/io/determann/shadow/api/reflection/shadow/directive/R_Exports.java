package io.determann.shadow.api.reflection.shadow.directive;

import io.determann.shadow.api.reflection.shadow.structure.R_Module;
import io.determann.shadow.api.reflection.shadow.structure.R_Package;
import io.determann.shadow.api.shadow.directive.C_Exports;

import java.util.List;

/**
 * Exports a {@link #getPackage()} to {@link #getTargetModules()} or {@link #toAll()}
 */
public non-sealed interface R_Exports extends R_Directive,
                                              C_Exports
{
   /**
    * packages to export to every module in {@link #getTargetModules()} or all if the list is empty
    */
   R_Package getPackage();

   /**
    * The list of modules the package is exported to. or all if the list is empty
    */
   List<R_Module> getTargetModules();

   boolean toAll();
}
