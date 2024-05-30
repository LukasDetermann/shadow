package io.determann.shadow.api.reflection.query;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.DeclaredHolder;
import io.determann.shadow.api.Documented;
import io.determann.shadow.api.shadow.Declared;

import java.util.List;

public interface PackageReflection extends ShadowReflection,
                                           NameableReflection,
                                           QualifiedNameableReflection,
                                           Annotationable,
                                           DeclaredHolder,
                                           ModuleEnclosedReflection,
                                           Documented
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