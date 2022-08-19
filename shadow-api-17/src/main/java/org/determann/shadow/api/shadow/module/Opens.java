package org.determann.shadow.api.shadow.module;

import org.determann.shadow.api.shadow.Module;
import org.determann.shadow.api.shadow.Package;

import java.util.List;

/**
 * Allows reflection access to {@link #getPackage()} for {@link #getTargetModules()} or {@link #toAll()}
 */
public interface Opens extends Directive
{
   /**
    * the package to be accessed by reflection
    */
   Package getPackage();

   /**
    * Modules allowed to acces {@link #getPackage()} or {@link #toAll()} if the list is empty
    */
   List<Module> getTargetModules();

   boolean toAll();
}
