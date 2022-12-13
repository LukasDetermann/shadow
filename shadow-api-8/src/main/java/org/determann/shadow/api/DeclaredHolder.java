package org.determann.shadow.api;

import org.determann.shadow.api.metadata.QualifiedName;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.*;

import static org.determann.shadow.api.ShadowApi.convert;

/**
 * Holds classes, interfaces, enums etc
 */
public interface DeclaredHolder extends ApiHolder
{
   Declared getDeclaredOrThrow(@QualifiedName String qualifiedName);

   default Annotation getAnnotationOrThrow(@QualifiedName String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toAnnotationOrThrow();
   }

   default Class getClassOrThrow(@QualifiedName String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toClassOrThrow();
   }

   default Enum getEnumOrThrow(@QualifiedName String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toEnumOrThrow();
   }

   default Interface getInterfaceOrThrow(@QualifiedName String qualifiedName)
   {
      return convert(getDeclaredOrThrow(qualifiedName)).toInterfaceThrow();
   }
}
