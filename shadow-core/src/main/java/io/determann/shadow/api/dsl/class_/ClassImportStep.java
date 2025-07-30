package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;

import java.util.Arrays;
import java.util.List;

public interface ClassImportStep
      extends ClassJavaDocStep
{
   ClassImportStep import_(String... name);

   default ClassImportStep import_(DeclaredRenderable... declared)
   {
      return import_(Arrays.asList(declared));
   }

   ClassImportStep import_(List<? extends DeclaredRenderable> declared);

   default ClassImportStep importPackage(PackageRenderable... cPackages)
   {
      return importPackage(Arrays.asList(cPackages));
   }

   ClassImportStep importPackage(List<? extends PackageRenderable> cPackages);

   ClassImportStep staticImport(String... name);

   default ClassImportStep staticImport(DeclaredRenderable... declared)
   {
      return staticImport(Arrays.asList(declared));
   }

   ClassImportStep staticImport(List<? extends DeclaredRenderable> declared);

   default ClassImportStep staticImportPackage(PackageRenderable... cPackages)
   {
      return staticImportPackage(Arrays.asList(cPackages));
   }

   ClassImportStep staticImportPackage(List<? extends PackageRenderable> cPackages);
}