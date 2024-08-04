package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.*;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.type.Declared;

import java.util.List;

public interface PackageLangModel extends Package,
                                          AnnotationableLangModel,
                                          NameableLangModel,
                                          QualifiedNameableLamgModel,
                                          ModuleEnclosedLangModel,
                                          DocumentedLangModel
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
