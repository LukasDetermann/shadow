package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.shadow.structure.C_Package;
import io.determann.shadow.api.shadow.type.C_Declared;

public interface ClassImportStep extends ClassJavaDocStep
{
   ClassImportStep import_(String... name);

   ClassImportStep import_(C_Declared... declared);

   ClassImportStep import_(C_Package... cPackages);

   ClassImportStep staticImport(String... name);

   ClassImportStep staticImport(C_Declared... declared);

   ClassImportStep staticImport(C_Package... cPackages);
}
