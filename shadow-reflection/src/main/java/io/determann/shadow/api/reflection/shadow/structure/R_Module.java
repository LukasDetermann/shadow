package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.R_Annotationable;
import io.determann.shadow.api.reflection.shadow.R_Nameable;
import io.determann.shadow.api.reflection.shadow.R_QualifiedNameable;
import io.determann.shadow.api.reflection.shadow.directive.R_Directive;
import io.determann.shadow.api.reflection.shadow.type.*;
import io.determann.shadow.api.shadow.C_TypeKind;
import io.determann.shadow.api.shadow.structure.C_Module;

import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.Provider.requestOrThrow;

public interface R_Module extends C_Module,
                                  R_Annotationable,
                                  R_Nameable,
                                  R_QualifiedNameable
{
   List<R_Package> getPackages();

   /**
    * can everybody use reflection on this module?
    */
   boolean isOpen();

   boolean isUnnamed();

   boolean isAutomatic();

   /**
    * Relations between modules
    */
   List<R_Directive> getDirectives();

   List<R_Declared> getDeclared();

   Optional<R_Declared> getDeclared(String qualifiedName);

   default R_Declared getDeclaredOrThrow(String qualifiedName)
   {
      return getDeclared(qualifiedName).orElseThrow();
   }

   default List<R_Annotation> getAnnotations()
   {
      return getDeclared().stream()
                          .filter(declared -> C_TypeKind.ANNOTATION.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
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
                          .filter(declared -> C_TypeKind.CLASS.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
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
                          .filter(declared -> C_TypeKind.ENUM.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
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
                          .filter(declared -> C_TypeKind.INTERFACE.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
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
                          .filter(declared -> C_TypeKind.RECORD.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
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
