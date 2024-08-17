package io.determann.shadow.api.lang_model.shadow.directive;

import io.determann.shadow.api.lang_model.shadow.structure.ModuleLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.PackageLangModel;
import io.determann.shadow.api.shadow.directive.Exports;

import java.util.List;

/**
 * Exports a {@link #getPackage()} to {@link #getTargetModules()} or {@link #toAll()}
 */
public non-sealed interface ExportsLangModel extends DirectiveLangModel,
                                                     Exports
{
   /**
    * packages to export to every module in {@link #getTargetModules()} or all if the list is empty
    */
   PackageLangModel getPackage();

   /**
    * The list of modules the package is exported to. or all if the list is empty
    */
   List<ModuleLangModel> getTargetModules();

   boolean toAll();
}
