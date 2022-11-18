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
      return convert(getDeclaredOrThrow(qualifiedName)).toAnnotation();
   }

   default @UnmodifiableView List<Annotation> getAnnotations()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ANNOTATION.equals(declared.getTypeKind()))
                          .map(declared -> convert(declared).toAnnotation())
                          .toList();
   }

   default Class getClassOrThrow(@QualifiedName String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toClass();
   }

   default @UnmodifiableView List<Class> getClasses()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.CLASS.equals(declared.getTypeKind()))
                          .map(declared -> convert(declared).toClass())
                          .toList();
   }

   default Enum getEnumOrThrow(@QualifiedName String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toEnum();
   }

   default @UnmodifiableView List<Enum> getEnums()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ENUM.equals(declared.getTypeKind()))
                          .map(declared -> convert(declared).toEnum())
                          .toList();
   }

   default Interface getInterfaceOrThrow(@QualifiedName String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toInterface();
   }

   default @UnmodifiableView List<Interface> getInterfaces()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.INTERFACE.equals(declared.getTypeKind()))
                          .map(declared -> convert(declared).toInterface())
                          .toList();
   }

   default Record getRecordOrThrow(@QualifiedName String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toRecord();
   }

   default @UnmodifiableView List<Record> getRecords()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.RECORD.equals(declared.getTypeKind()))
                          .map(declared -> convert(declared).toRecord())
                          .toList();
   }
}
