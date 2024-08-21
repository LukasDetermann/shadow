package io.determann.shadow.api.reflection.shadow.directive;

import io.determann.shadow.api.reflection.shadow.structure.R_Module;
import io.determann.shadow.api.reflection.shadow.structure.R_Package;
import io.determann.shadow.api.shadow.directive.C_Opens;

import java.util.List;

/**
 * Allows reflection access to {@link #getPackage()} for {@link #getTargetModules()} or {@link #toAll()}
 */
public non-sealed interface R_Opens extends R_Directive,
                                            C_Opens
{
   /**
    * the package to be accessed by reflection
    */
   R_Package getPackage();

   /**
    * Modules allowed to access {@link #getPackage()} or {@link #toAll()} if the list is empty
    */
   List<R_Module> getTargetModules();

   boolean toAll();
}
