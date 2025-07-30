package io.determann.shadow.api.dsl.interface_;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;

import java.util.Arrays;
import java.util.List;

public interface InterfaceImportStep
      extends InterfaceJavaDocStep
{
   InterfaceImportStep import_(String... name);

   default InterfaceImportStep import_(DeclaredRenderable... declared)
   {
      return import_(Arrays.asList(declared));
   }

   InterfaceImportStep import_(List<? extends DeclaredRenderable> declared);

   default InterfaceImportStep importPackage(PackageRenderable... cPackages)
   {
      return importPackage(Arrays.asList(cPackages));
   }

   InterfaceImportStep importPackage(List<? extends PackageRenderable> cPackages);

   InterfaceImportStep staticImport(String... name);

   default InterfaceImportStep staticImport(DeclaredRenderable... declared)
   {
      return staticImport(Arrays.asList(declared));
   }

   InterfaceImportStep staticImport(List<? extends DeclaredRenderable> declared);

   default InterfaceImportStep staticImportPackage(PackageRenderable... cPackages)
   {
      return staticImportPackage(Arrays.asList(cPackages));
   }

   InterfaceImportStep staticImportPackage(List<? extends PackageRenderable> cPackages);
}