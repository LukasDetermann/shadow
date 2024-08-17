package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.AnnotationableReflection;
import io.determann.shadow.api.reflection.shadow.NameableReflection;
import io.determann.shadow.api.reflection.shadow.QualifiedNameableReflection;
import io.determann.shadow.api.reflection.shadow.type.*;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.structure.Package;

import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.shadow.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public interface PackageReflection extends Package,
                                           AnnotationableReflection,
                                           NameableReflection,
                                           QualifiedNameableReflection,
                                           ModuleEnclosedReflection
{
   /**
    * Unnamed packages are intend for small snips of code like jShell and not seen in regular projects
    */
   boolean isUnnamed();

   List<DeclaredReflection> getDeclared();

   Optional<DeclaredReflection> getDeclared(String qualifiedName);

   default DeclaredReflection getDeclaredOrThrow(String qualifiedName)
   {
      return getDeclared(qualifiedName).orElseThrow();
   }

   default List<AnnotationReflection> getAnnotations()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ANNOTATION.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((AnnotationReflection) declared))
                          .toList();
   }

   default Optional<AnnotationReflection> getAnnotation(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(AnnotationReflection.class::cast);
   }

   default AnnotationReflection getAnnotationOrThrow(String qualifiedName)
   {
      return ((AnnotationReflection) getDeclaredOrThrow(qualifiedName));
   }

   default List<ClassReflection> getClasses()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.CLASS.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((ClassReflection) declared))
                          .toList();
   }

   default Optional<ClassReflection> getClass(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(ClassReflection.class::cast);
   }

   default ClassReflection getClassOrThrow(String qualifiedName)
   {
      return ((ClassReflection) getDeclaredOrThrow(qualifiedName));
   }

   default List<EnumReflection> getEnums()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ENUM.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((EnumReflection) declared))
                          .toList();
   }

   default Optional<EnumReflection> getEnum(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(EnumReflection.class::cast);
   }

   default EnumReflection getEnumOrThrow(String qualifiedName)
   {
      return ((EnumReflection) getDeclaredOrThrow(qualifiedName));
   }

   default List<InterfaceReflection> getInterfaces()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.INTERFACE.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((InterfaceReflection) declared))
                          .toList();
   }

   default Optional<InterfaceReflection> getInterface(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(InterfaceReflection.class::cast);
   }

   default InterfaceReflection getInterfaceOrThrow(String qualifiedName)
   {
      return ((InterfaceReflection) getDeclaredOrThrow(qualifiedName));
   }

   default List<RecordReflection> getRecords()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.RECORD.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((RecordReflection) declared))
                          .toList();
   }

   default Optional<RecordReflection> getRecord(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(RecordReflection.class::cast);
   }

   default RecordReflection getRecordOrThrow(String qualifiedName)
   {
      return ((RecordReflection) getDeclaredOrThrow(qualifiedName));
   }
}
