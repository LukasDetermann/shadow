package org.determann.shadow.api;

import org.determann.shadow.api.metadata.QualifiedName;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.*;

/**
 * Holds classes, interfaces, enums etc
 */
public interface DeclaredHolder extends ApiHolder
{
   Declared getDeclared(@QualifiedName String qualifiedName);

   default Annotation getAnnotation(@QualifiedName String qualifiedName)
   {
      return getApi().convert(getDeclared(qualifiedName)).toAnnotation();
   }

   default Class getClass(@QualifiedName String qualifiedName)
   {
      return getApi().convert(getDeclared(qualifiedName)).toClass();
   }

   default Enum getEnum(@QualifiedName String qualifiedName)
   {
      return getApi().convert(getDeclared(qualifiedName)).toEnum();
   }

   default Interface getInterface(@QualifiedName String qualifiedName)
   {
      return getApi().convert(getDeclared(qualifiedName)).toInterface();
   }
}
