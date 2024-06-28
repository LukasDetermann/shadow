package io.determann.shadow.api.shadow.modifier;

public interface AccessModifiable extends Modifiable
{
   default boolean isPublic()
   {
      return hasModifier(Modifier.PUBLIC);
   }

   default boolean isPackagePrivate()
   {
      return !hasModifier(Modifier.PUBLIC) && !hasModifier(Modifier.PROTECTED) && !hasModifier(Modifier.PRIVATE);
   }

   default boolean isProtected()
   {
      return hasModifier(Modifier.PROTECTED);
   }

   default boolean isPrivate()
   {
      return hasModifier(Modifier.PRIVATE);
   }
}
