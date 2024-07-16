package io.determann.shadow.api.shadow.module;

import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;

import java.util.List;

/**
 * Allows reflection access to {@link #getPackage()} for {@link #getTargetModules()} or {@link #toAll()}
 */
public non-sealed interface Opens extends Directive
{
   /**
    * the package to be accessed by reflection
    */
   Package getPackage();

   /**
    * Modules allowed to access {@link #getPackage()} or {@link #toAll()} if the list is empty
    */
   List<Module> getTargetModules();

   boolean toAll();
}
