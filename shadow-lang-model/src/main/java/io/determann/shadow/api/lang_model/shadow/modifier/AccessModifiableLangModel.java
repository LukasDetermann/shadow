package io.determann.shadow.api.lang_model.shadow.modifier;

import io.determann.shadow.api.shadow.modifier.AccessModifiable;

import static io.determann.shadow.api.shadow.modifier.Modifier.*;

public interface AccessModifiableLangModel extends ModifiableLangModel,
                                                   AccessModifiable
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
