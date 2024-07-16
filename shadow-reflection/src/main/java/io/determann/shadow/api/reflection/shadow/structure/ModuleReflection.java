package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.NameableReflection;
import io.determann.shadow.api.reflection.shadow.QualifiedNameableReflection;
import io.determann.shadow.api.reflection.shadow.type.ShadowReflection;
import io.determann.shadow.api.shadow.module.Directive;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;

import java.util.List;

public interface ModuleReflection extends Module,
                                          ShadowReflection,
                                          NameableReflection,
                                          QualifiedNameableReflection
{
   List<Package> getPackages();

   /**
    * can everybody use reflection on this module?
    */
   boolean isOpen();

   boolean isUnnamed();

   boolean isAutomatic();

   /**
    * Relations between modules
    */
   List<Directive> getDirectives();
}
