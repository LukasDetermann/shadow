package io.determann.shadow.api;

import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.*;

import java.util.NoSuchElementException;
import java.util.Optional;

import static io.determann.shadow.api.ShadowApi.convert;

/**
 * Holds classes, interfaces, enums etc
 */
public interface DeclaredHolder extends ApiHolder
{
   Optional<Declared> getDeclared(String qualifiedName);

   default Declared getDeclaredOrThrow(String qualifiedName)
   {
      return getDeclared(qualifiedName).orElseThrow(NoSuchElementException::new);
   }

   default Optional<Annotation> getAnnotation(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toAnnotation();
   }

   default Annotation getAnnotationOrThrow(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toAnnotationOrThrow();
   }

   default Optional<Class> getClass(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toClass();
   }

   default Class getClassOrThrow(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toClassOrThrow();
   }

   default Optional<Enum> getEnum(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toEnum();
   }

   default Enum getEnumOrThrow(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toEnumOrThrow();
   }

   default Optional<Interface> getInterface(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toInterface();
   }

   default Interface getInterfaceOrThrow(String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toInterfaceOrThrow();
   }
}
