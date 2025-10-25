package io.determann.shadow.internal.dsl;

import org.jetbrains.annotations.Nullable;

public record QualifiedName(@Nullable String packageName,
                            String typeName)
{
   String qualifiedName()
   {
      return packageName + '.' + typeName;
   }

   String importableName()
   {
      int dotIndex = typeName.indexOf('.');
      if (dotIndex == -1)
      {
         return qualifiedName();
      }
      return packageName + '.' + typeName.substring(0, dotIndex);
   }
}