package io.determann.shadow.api.reflection.shadow.directive;

import io.determann.shadow.api.reflection.shadow.structure.ModuleReflection;
import io.determann.shadow.api.reflection.shadow.structure.PackageReflection;
import io.determann.shadow.api.shadow.directive.Exports;

import java.util.List;

/**
 * Exports a {@link #getPackage()} to {@link #getTargetModules()} or {@link #toAll()}
 */
public non-sealed interface ExportsReflection extends DirectiveReflection,
                                                      Exports
{
   /**
    * packages to export to every module in {@link #getTargetModules()} or all if the list is empty
    */
   PackageReflection getPackage();

   /**
    * The list of modules the package is exported to. or all if the list is empty
    */
   List<ModuleReflection> getTargetModules();

   boolean toAll();
}
