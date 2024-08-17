package io.determann.shadow.api.lang_model.shadow.directive;

import io.determann.shadow.api.lang_model.shadow.structure.ModuleLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.PackageLangModel;
import io.determann.shadow.api.shadow.directive.Opens;

import java.util.List;

/**
 * Allows reflection access to {@link #getPackage()} for {@link #getTargetModules()} or {@link #toAll()}
 */
public non-sealed interface OpensLangModel extends DirectiveLangModel,
                                                   Opens
{
   /**
    * the package to be accessed by reflection
    */
   PackageLangModel getPackage();

   /**
    * Modules allowed to access {@link #getPackage()} or {@link #toAll()} if the list is empty
    */
   List<ModuleLangModel> getTargetModules();

   boolean toAll();
}
