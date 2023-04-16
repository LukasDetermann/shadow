package io.determann.shadow.api;

import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.*;

/**
 * Holds classes, interfaces, enums etc
 */
public interface DeclaredHolder extends ApiHolder
{
   Declared getDeclaredOrThrow(String qualifiedName);

   default Annotation getAnnotationOrThrow(String qualifiedName)
   {
      return ShadowApi.convert(getDeclaredOrThrow(qualifiedName)).toAnnotationOrThrow();
   }

   default Class getClassOrThrow(String qualifiedName)
   {
      return ShadowApi.convert(getDeclaredOrThrow(qualifiedName)).toClassOrThrow();
   }

   default Enum getEnumOrThrow(String qualifiedName)
   {
      return ShadowApi.convert(getDeclaredOrThrow(qualifiedName)).toEnumOrThrow();
   }

   default Interface getInterfaceOrThrow(String qualifiedName)
   {
      return ShadowApi.convert(getDeclaredOrThrow(qualifiedName)).toInterfaceOrThrow();
   }
}
