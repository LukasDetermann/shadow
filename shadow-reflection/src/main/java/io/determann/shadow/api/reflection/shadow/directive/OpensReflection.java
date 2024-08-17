package io.determann.shadow.api.reflection.shadow.directive;

import io.determann.shadow.api.reflection.shadow.structure.ModuleReflection;
import io.determann.shadow.api.reflection.shadow.structure.PackageReflection;
import io.determann.shadow.api.shadow.directive.Opens;

import java.util.List;

/**
 * Allows reflection access to {@link #getPackage()} for {@link #getTargetModules()} or {@link #toAll()}
 */
public non-sealed interface OpensReflection extends DirectiveReflection,
                                                    Opens
{
   /**
    * the package to be accessed by reflection
    */
   PackageReflection getPackage();

   /**
    * Modules allowed to access {@link #getPackage()} or {@link #toAll()} if the list is empty
    */
   List<ModuleReflection> getTargetModules();

   boolean toAll();
}
