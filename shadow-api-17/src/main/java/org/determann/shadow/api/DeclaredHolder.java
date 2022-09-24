package org.determann.shadow.api;

import org.determann.shadow.api.metadata.QualifiedName;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.Record;
import org.determann.shadow.api.shadow.*;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;

/**
 * Holds classes, interfaces, enums etc
 */
public interface DeclaredHolder extends ApiHolder
{
   @UnmodifiableView List<Declared> getDeclared();

   Declared getDeclared(@QualifiedName String qualifiedName);

   default Annotation getAnnotation(@QualifiedName String qualifiedName)
   {
      return getApi().convert(getDeclared(qualifiedName))
                     .toOptionalAnnotation()
                     .orElseThrow(() -> new IllegalArgumentException("No Annotation found for " + qualifiedName));
   }

   default @UnmodifiableView List<Annotation> getAnnotations()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ANNOTATION.equals(declared.getTypeKind()))
                          .map(declared -> getApi().convert(declared).toOptionalAnnotation().orElseThrow())
                          .toList();
   }

   default Class getClass(@QualifiedName String qualifiedName)
   {
      return getApi().convert(getDeclared(qualifiedName))
                     .toOptionalClass()
                     .orElseThrow(() -> new IllegalArgumentException("No Class found for " + qualifiedName));
   }

   default @UnmodifiableView List<Class> getClasses()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.CLASS.equals(declared.getTypeKind()))
                          .map(declared -> getApi().convert(declared).toOptionalClass().orElseThrow())
                          .toList();
   }

   default Enum getEnum(@QualifiedName String qualifiedName)
   {
      return getApi().convert(getDeclared(qualifiedName))
                     .toOptionalEnum()
                     .orElseThrow(() -> new IllegalArgumentException("No getEnum found for " + qualifiedName));
   }

   default @UnmodifiableView List<Enum> getEnums()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ENUM.equals(declared.getTypeKind()))
                          .map(declared -> getApi().convert(declared).toOptionalEnum().orElseThrow())
                          .toList();
   }

   default Interface getInterface(@QualifiedName String qualifiedName)
   {
      return getApi().convert(getDeclared(qualifiedName))
                     .toOptionalInterface()
                     .orElseThrow(() -> new IllegalArgumentException("No Interface found for " + qualifiedName));
   }

   default @UnmodifiableView List<Interface> getInterfaces()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.INTERFACE.equals(declared.getTypeKind()))
                          .map(declared -> getApi().convert(declared).toOptionalInterface().orElseThrow())
                          .toList();
   }

   default Record getRecord(@QualifiedName String qualifiedName)
   {
      return getApi().convert(getDeclared(qualifiedName))
                     .toOptionalRecord()
                     .orElseThrow(() -> new IllegalArgumentException("No Record found for " + qualifiedName));
   }

   default @UnmodifiableView List<Record> getRecords()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.RECORD.equals(declared.getTypeKind()))
                          .map(declared -> getApi().convert(declared).toOptionalRecord().orElseThrow())
                          .toList();
   }
}
