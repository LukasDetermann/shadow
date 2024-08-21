package io.determann.shadow.api.lang_model.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.C_AccessModifiable;

import static io.determann.shadow.api.shadow.modifier.C_Modifier.*;

public interface LM_AccessModifiable extends LM_Modifiable,
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
