package io.determann.shadow.api.shadow;

import io.determann.shadow.api.*;

import java.util.List;

public interface Package extends Shadow,
                                 Nameable,
                                 QualifiedNameable,
                                 Annotationable,
                                 DeclaredHolder,
                                 ModuleEnclosed,
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
