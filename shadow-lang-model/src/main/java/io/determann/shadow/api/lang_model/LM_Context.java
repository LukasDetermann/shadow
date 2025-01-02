package io.determann.shadow.api.lang_model;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Module;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Package;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.shadow.structure.C_Module;
import io.determann.shadow.api.shadow.type.C_Enum;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.api.shadow.type.C_Record;

import java.util.List;
import java.util.Optional;

public interface LM_Context
{
   Implementation getImplementation();

   List<LM_Declared> getDeclared();

   Optional<LM_Declared> getDeclared(String qualifiedName);

   default LM_Declared getDeclaredOrThrow(String qualifiedName)
   {
      return getDeclared(qualifiedName).orElseThrow();
   }

   default List<LM_Annotation> getAnnotations()
   {
      return getDeclared().stream()
                          .filter(declared -> declared instanceof LM_Annotation)
                          .map(declared -> ((LM_Annotation) declared))
                          .toList();
   }

   default Optional<LM_Annotation> getAnnotation(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(LM_Annotation.class::cast);
   }

   default LM_Annotation getAnnotationOrThrow(String qualifiedName)
   {
      return ((LM_Annotation) getDeclaredOrThrow(qualifiedName));
   }

   default List<LM_Class> getClasses()
   {
      return getDeclared().stream()
                          .filter(declared -> declared instanceof LM_Class)
                          .map(declared -> ((LM_Class) declared))
                          .toList();
   }

   default Optional<LM_Class> getClass(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(LM_Class.class::cast);
   }

   default LM_Class getClassOrThrow(String qualifiedName)
   {
      return ((LM_Class) getDeclaredOrThrow(qualifiedName));
   }

   default List<LM_Enum> getEnums()
   {
      return getDeclared().stream()
                          .filter(declared -> declared instanceof C_Enum)
                          .map(declared -> ((LM_Enum) declared))
                          .toList();
   }

   default Optional<LM_Enum> getEnum(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(LM_Enum.class::cast);
   }

   default LM_Enum getEnumOrThrow(String qualifiedName)
   {
      return ((LM_Enum) getDeclaredOrThrow(qualifiedName));
   }

   default List<LM_Interface> getInterfaces()
   {
      return getDeclared().stream()
                          .filter(declared -> declared instanceof C_Interface)
                          .map(declared -> ((LM_Interface) declared))
                          .toList();
   }

   default Optional<LM_Interface> getInterface(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(LM_Interface.class::cast);
   }

   default LM_Interface getInterfaceOrThrow(String qualifiedName)
   {
      return ((LM_Interface) getDeclaredOrThrow(qualifiedName));
   }

   default List<LM_Record> getRecords()
   {
      return getDeclared().stream()
                          .filter(declared -> declared instanceof C_Record)
                          .map(declared -> ((LM_Record) declared))
                          .toList();
   }

   default Optional<LM_Record> getRecord(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(LM_Record.class::cast);
   }

   default LM_Record getRecordOrThrow(String qualifiedName)
   {
      return ((LM_Record) getDeclaredOrThrow(qualifiedName));
   }

   List<LM_Module> getModules();

   Optional<LM_Module> getModule(String name);

   LM_Module getModuleOrThrow(String name);

   /**
    * a package is unique per module. With multiple modules there can be multiple packages with the same name
    */
   List<LM_Package> getPackage(String qualifiedName);

   List<LM_Package> getPackages();

   Optional<LM_Package> getPackage(String qualifiedModuleName, String qualifiedPackageName);

   LM_Package getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName);

   Optional<LM_Package> getPackage(C_Module module, String qualifiedPackageName);

   LM_Package getPackageOrThrow(C_Module module, String qualifiedPackageName);

   LM_Constants getConstants();
}
