package org.determann.shadow.api;

import org.determann.shadow.api.metadata.QualifiedName;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.*;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;
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
                          .collect(toUnmodifiableList());
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
                          .collect(toUnmodifiableList());
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
                          .collect(toUnmodifiableList());
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
                          .collect(toUnmodifiableList());
   }
}
