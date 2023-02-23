package org.determann.shadow.api;

import org.determann.shadow.api.metadata.QualifiedName;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.Record;
import org.determann.shadow.api.shadow.*;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;

import static org.determann.shadow.api.ShadowApi.convert;

/**
 * Holds classes, interfaces, enums etc
 */
public interface DeclaredHolder extends ApiHolder
{
   @UnmodifiableView List<Declared> getDeclared();

   Declared getDeclaredOrThrow(@QualifiedName String qualifiedName);

   default Annotation getAnnotationOrThrow(@QualifiedName String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toAnnotationOrThrow();
   }

   default @UnmodifiableView List<Annotation> getAnnotations()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ANNOTATION.equals(declared.getTypeKind()))
                          .map(declared -> convert(declared).toAnnotationOrThrow())
                          .toList();
   }

   default Class getClassOrThrow(@QualifiedName String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toClassOrThrow();
   }

   default @UnmodifiableView List<Class> getClasses()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.CLASS.equals(declared.getTypeKind()))
                          .map(declared -> convert(declared).toClassOrThrow())
                          .toList();
   }

   default Enum getEnumOrThrow(@QualifiedName String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toEnumOrThrow();
   }

   default @UnmodifiableView List<Enum> getEnums()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ENUM.equals(declared.getTypeKind()))
                          .map(declared -> convert(declared).toEnumOrThrow())
                          .toList();
   }

   default Interface getInterfaceOrThrow(@QualifiedName String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toInterfaceOrThrow();
   }

   default @UnmodifiableView List<Interface> getInterfaces()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.INTERFACE.equals(declared.getTypeKind()))
                          .map(declared -> convert(declared).toInterfaceOrThrow())
                          .toList();
   }

   default Record getRecordOrThrow(@QualifiedName String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toRecordOrThrow();
   }

   default @UnmodifiableView List<Record> getRecords()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.RECORD.equals(declared.getTypeKind()))
                          .map(declared -> convert(declared).toRecordOrThrow())
                          .toList();
   }
}
