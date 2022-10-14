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
   Declared getDeclared(@QualifiedName String qualifiedName);

   default Annotation getAnnotation(@QualifiedName String qualifiedName)
   {
      return convert(getDeclared(qualifiedName)).toAnnotation();
   }

   default Class getClass(@QualifiedName String qualifiedName)
   {
      return convert(getDeclared(qualifiedName)).toClass();
   }

   default Enum getEnum(@QualifiedName String qualifiedName)
   {
      return convert(getDeclared(qualifiedName)).toEnum();
   }

   default Interface getInterface(@QualifiedName String qualifiedName)
   {
      return convert(getDeclared(qualifiedName)).toInterface();
   }
}
