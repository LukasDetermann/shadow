package io.determann.shadow.api.dsl.enum_;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;

import java.util.Arrays;
import java.util.List;

public interface EnumImportStep
      extends EnumJavaDocStep
{
   EnumImportStep import_(String... name);

   default EnumImportStep import_(DeclaredRenderable... declared)
   {
      return import_(Arrays.asList(declared));
   }

   EnumImportStep import_(List<? extends DeclaredRenderable> declared);

   default EnumImportStep importPackage(PackageRenderable... cPackages)
   {
      return importPackage(Arrays.asList(cPackages));
   }

   EnumImportStep importPackage(List<? extends PackageRenderable> cPackages);

   EnumImportStep staticImport(String... name);

   default EnumImportStep staticImport(DeclaredRenderable... declared)
   {
      return staticImport(Arrays.asList(declared));
   }

   EnumImportStep staticImport(List<? extends DeclaredRenderable> declared);

   default EnumImportStep staticImportPackage(PackageRenderable... cPackages)
   {
      return staticImportPackage(Arrays.asList(cPackages));
   }

   EnumImportStep staticImportPackage(List<? extends PackageRenderable> cPackages);
}