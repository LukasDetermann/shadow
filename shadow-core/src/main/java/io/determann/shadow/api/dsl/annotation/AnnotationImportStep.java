package io.determann.shadow.api.dsl.annotation;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;

import java.util.Arrays;
import java.util.List;

public interface AnnotationImportStep
      extends AnnotationJavaDocStep
{
   AnnotationImportStep import_(String... name);

   default AnnotationImportStep import_(DeclaredRenderable... declared)
   {
      return import_(Arrays.asList(declared));
   }

   AnnotationImportStep import_(List<? extends DeclaredRenderable> declared);

   default AnnotationImportStep importPackage(PackageRenderable... cPackages)
   {
      return importPackage(Arrays.asList(cPackages));
   }

   AnnotationImportStep importPackage(List<? extends PackageRenderable> cPackages);

   AnnotationImportStep staticImport(String... name);

   default AnnotationImportStep staticImport(DeclaredRenderable... declared)
   {
      return staticImport(Arrays.asList(declared));
   }

   AnnotationImportStep staticImport(List<? extends DeclaredRenderable> declared);

   default AnnotationImportStep staticImportPackage(PackageRenderable... cPackages)
   {
      return staticImportPackage(Arrays.asList(cPackages));
   }

   AnnotationImportStep staticImportPackage(List<? extends PackageRenderable> cPackages);
}