package io.determann.shadow.api.shadow.module;

import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;

import java.util.List;

/**
 * Exports a {@link #getPackage()} to {@link #getTargetModules()} or {@link #toAll()}
 */
public non-sealed interface Exports extends Directive
{
   /**
    * packages to export to every module in {@link #getTargetModules()} or all if the list is empty
    */
   Package getPackage();

   /**
    * The list of modules the package is exported to. or all if the list is empty
    */
   List<Module> getTargetModules();

   boolean toAll();
}
