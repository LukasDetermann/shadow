package io.determann.shadow.api.reflection.query;

import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Package;

import java.util.List;

public interface PackageReflection extends Package,
                                           ShadowReflection,
                                           NameableReflection,
                                           QualifiedNameableReflection,
                                           ModuleEnclosedReflection
{
   /**
    * returns everything in this package
    */
   List<Declared> getContent();

   /**
    * Unnamed packages are intend for small snips of code like jShell and not seen in regular projects
    */
   boolean isUnnamed();
}
