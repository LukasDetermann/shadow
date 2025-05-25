package io.determann.shadow.api.dsl.annotation;

import io.determann.shadow.api.shadow.structure.C_Package;
import io.determann.shadow.api.shadow.type.C_Declared;

public interface AnnotationImportStep
      extends AnnotationJavaDocStep
{
   AnnotationImportStep import_(String... name);

   AnnotationImportStep import_(C_Declared... declared);

   AnnotationImportStep import_(C_Package... cPackages);

   AnnotationImportStep staticImport(String... name);

   AnnotationImportStep staticImport(C_Declared... declared);

   AnnotationImportStep staticImport(C_Package... cPackages);
}
