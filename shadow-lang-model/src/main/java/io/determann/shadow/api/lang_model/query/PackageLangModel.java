package io.determann.shadow.api.lang_model.query;

import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Package;

import java.util.List;

public interface PackageLangModel extends Package,
                                          ShadowLangModel,
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
