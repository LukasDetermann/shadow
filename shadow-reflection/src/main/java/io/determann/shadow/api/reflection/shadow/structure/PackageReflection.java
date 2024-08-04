package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.AnnotationableReflection;
import io.determann.shadow.api.reflection.shadow.NameableReflection;
import io.determann.shadow.api.reflection.shadow.QualifiedNameableReflection;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.type.Declared;

import java.util.List;

public interface PackageReflection extends Package,
                                           AnnotationableReflection,
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
