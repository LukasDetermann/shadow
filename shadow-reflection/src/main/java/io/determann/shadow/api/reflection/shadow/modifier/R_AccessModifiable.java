package io.determann.shadow.api.reflection.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.C_AccessModifiable;

import static io.determann.shadow.api.shadow.modifier.C_Modifier.*;

public interface R_AccessModifiable extends R_Modifiable,
                                            C_AccessModifiable
{
   default boolean isPublic()
   {
      return hasModifier(PUBLIC);
   }

   default boolean isPackagePrivate()
   {
      return hasModifier(PACKAGE_PRIVATE);
   }

   default boolean isProtected()
   {
      return hasModifier(PROTECTED);
   }

   default boolean isPrivate()
   {
      return hasModifier(PRIVATE);
   }
}
