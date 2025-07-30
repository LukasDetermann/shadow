package io.determann.shadow.api.dsl.record;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;

import java.util.Arrays;
import java.util.List;

public interface RecordImportStep
      extends RecordJavaDocStep
{
   RecordImportStep import_(String... name);

   default RecordImportStep import_(DeclaredRenderable... declared)
   {
      return import_(Arrays.asList(declared));
   }

   RecordImportStep import_(List<? extends DeclaredRenderable> declared);

   default RecordImportStep importPackage(PackageRenderable... cPackages)
   {
      return importPackage(Arrays.asList(cPackages));
   }

   RecordImportStep importPackage(List<? extends PackageRenderable> cPackages);

   RecordImportStep staticImport(String... name);

   default RecordImportStep staticImport(DeclaredRenderable... declared)
   {
      return staticImport(Arrays.asList(declared));
   }

   RecordImportStep staticImport(List<? extends DeclaredRenderable> declared);

   default RecordImportStep staticImportPackage(PackageRenderable... cPackages)
   {
      return staticImportPackage(Arrays.asList(cPackages));
   }

   RecordImportStep staticImportPackage(List<? extends PackageRenderable> cPackages);
}