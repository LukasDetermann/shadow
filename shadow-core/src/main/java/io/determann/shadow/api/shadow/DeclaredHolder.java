package io.determann.shadow.api.shadow;

import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.*;

import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.api.shadow.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

/**
 * Holds classes, interfaces, enums etc
 */
public interface DeclaredHolder
{
   List<Declared> getDeclared();

   Optional<Declared> getDeclared(String qualifiedName);

   default Declared getDeclaredOrThrow(String qualifiedName)
   {
      return getDeclared(qualifiedName).orElseThrow();
   }

   default List<Annotation> getAnnotations()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ANNOTATION.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> convert(declared).toAnnotationOrThrow())
                          .toList();
   }

   default Optional<Annotation> getAnnotation(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toAnnotation();
   }

   default Annotation getAnnotationOrThrow(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toAnnotationOrThrow();
   }

   default List<Class> getClasses()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.CLASS.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> convert(declared).toClassOrThrow())
                          .toList();
   }

   default Optional<Class> getClass(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toClass();
   }

   default Class getClassOrThrow(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toClassOrThrow();
   }

   default List<Enum> getEnums()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ENUM.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> convert(declared).toEnumOrThrow())
                          .toList();
   }

   default Optional<Enum> getEnum(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toEnum();
   }

   default Enum getEnumOrThrow(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toEnumOrThrow();
   }

   default List<Interface> getInterfaces()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.INTERFACE.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> convert(declared).toInterfaceOrThrow())
                          .toList();
   }

   default Optional<Interface> getInterface(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toInterface();
   }

   default Interface getInterfaceOrThrow(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toInterfaceOrThrow();
   }

   default List<Record> getRecords()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.RECORD.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> convert(declared).toRecordOrThrow())
                          .toList();
   }

   default Optional<Record> getRecord(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toRecord();
   }

   default Record getRecordOrThrow(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toRecordOrThrow();
   }
}
