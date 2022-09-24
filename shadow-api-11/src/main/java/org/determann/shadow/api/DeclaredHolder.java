package org.determann.shadow.api;

import org.determann.shadow.api.metadata.QualifiedName;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.*;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

/**
 * Holds classes, interfaces, enums etc
 */
public interface DeclaredHolder extends ApiHolder
{
   @UnmodifiableView List<Declared> getDeclared();

   Declared getDeclared(@QualifiedName String qualifiedName);

   default Annotation getAnnotation(@QualifiedName String qualifiedName)
   {
      return getApi().convert(getDeclared(qualifiedName)).toAnnotation();
   }

   default @UnmodifiableView List<Annotation> getAnnotations()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ANNOTATION.equals(declared.getTypeKind()))
                          .map(declared -> getApi().convert(declared).toAnnotation())
                          .collect(toUnmodifiableList());
   }

   default Class getClass(@QualifiedName String qualifiedName)
   {
      return getApi().convert(getDeclared(qualifiedName)).toClass();
   }

   default @UnmodifiableView List<Class> getClasses()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.CLASS.equals(declared.getTypeKind()))
                          .map(declared -> getApi().convert(declared).toClass())
                          .collect(toUnmodifiableList());
   }

   default Enum getEnum(@QualifiedName String qualifiedName)
   {
      return getApi().convert(getDeclared(qualifiedName)).toEnum();
   }

   default @UnmodifiableView List<Enum> getEnums()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ENUM.equals(declared.getTypeKind()))
                          .map(declared -> getApi().convert(declared).toEnum())
                          .collect(toUnmodifiableList());
   }

   default Interface getInterface(@QualifiedName String qualifiedName)
   {
      return getApi().convert(getDeclared(qualifiedName)).toInterface();
   }

   default @UnmodifiableView List<Interface> getInterfaces()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.INTERFACE.equals(declared.getTypeKind()))
                          .map(declared -> getApi().convert(declared).toInterface())
                          .collect(toUnmodifiableList());
   }
}
