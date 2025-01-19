package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.R_Annotationable;
import io.determann.shadow.api.reflection.shadow.R_Nameable;
import io.determann.shadow.api.reflection.shadow.R_QualifiedNameable;
import io.determann.shadow.api.reflection.shadow.type.*;
import io.determann.shadow.api.shadow.structure.C_Package;

import java.util.List;
import java.util.Optional;

public interface R_Package

      extends C_Package,
              R_Annotationable,
              R_Nameable,
              R_QualifiedNameable,
              R_ModuleEnclosed
{
   /**
    * Unnamed packages are intend for small snips of code like jShell and not seen in regular projects
    */
   boolean isUnnamed();

   List<R_Declared> getDeclared();

   Optional<R_Declared> getDeclared(String qualifiedName);

   default R_Declared getDeclaredOrThrow(String qualifiedName)
   {
      return getDeclared(qualifiedName).orElseThrow();
   }

   default List<R_Annotation> getAnnotations()
   {
      return getDeclared().stream()
                          .filter(declared -> declared instanceof R_Annotation)
                          .map(declared -> ((R_Annotation) declared))
                          .toList();
   }

   default Optional<R_Annotation> getAnnotation(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(R_Annotation.class::cast);
   }

   default R_Annotation getAnnotationOrThrow(String qualifiedName)
   {
      return ((R_Annotation) getDeclaredOrThrow(qualifiedName));
   }

   default List<R_Class> getClasses()
   {
      return getDeclared().stream()
                          .filter(declared -> declared instanceof R_Class)
                          .map(declared -> ((R_Class) declared))
                          .toList();
   }

   default Optional<R_Class> getClass(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(R_Class.class::cast);
   }

   default R_Class getClassOrThrow(String qualifiedName)
   {
      return ((R_Class) getDeclaredOrThrow(qualifiedName));
   }

   default List<R_Enum> getEnums()
   {
      return getDeclared().stream()
                          .filter(declared -> declared instanceof R_Enum)
                          .map(declared -> ((R_Enum) declared))
                          .toList();
   }

   default Optional<R_Enum> getEnum(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(R_Enum.class::cast);
   }

   default R_Enum getEnumOrThrow(String qualifiedName)
   {
      return ((R_Enum) getDeclaredOrThrow(qualifiedName));
   }

   default List<R_Interface> getInterfaces()
   {
      return getDeclared().stream()
                          .filter(declared -> declared instanceof R_Interface)
                          .map(declared -> ((R_Interface) declared))
                          .toList();
   }

   default Optional<R_Interface> getInterface(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(R_Interface.class::cast);
   }

   default R_Interface getInterfaceOrThrow(String qualifiedName)
   {
      return ((R_Interface) getDeclaredOrThrow(qualifiedName));
   }

   default List<R_Record> getRecords()
   {
      return getDeclared().stream()
                          .filter(declared -> declared instanceof R_Record)
                          .map(declared -> ((R_Record) declared))
                          .toList();
   }

   default Optional<R_Record> getRecord(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(R_Record.class::cast);
   }

   default R_Record getRecordOrThrow(String qualifiedName)
   {
      return ((R_Record) getDeclaredOrThrow(qualifiedName));
   }
}
