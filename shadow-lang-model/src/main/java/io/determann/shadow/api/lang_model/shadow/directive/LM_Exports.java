package io.determann.shadow.api.lang_model.shadow.directive;

import io.determann.shadow.api.lang_model.shadow.structure.LM_Module;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Package;
import io.determann.shadow.api.shadow.directive.C_Exports;

import java.util.List;

/**
 * Exports a {@link #getPackage()} to {@link #getTargetModules()} or {@link #toAll()}
 */
public non-sealed interface LM_Exports extends LM_Directive,
                                               C_Exports
{
   /**
    * packages to export to every module in {@link #getTargetModules()} or all if the list is empty
    */
   LM_Package getPackage();

   /**
    * The list of modules the package is exported to. or all if the list is empty
    */
   List<LM_Module> getTargetModules();

   boolean toAll();
}
