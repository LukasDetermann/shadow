package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.modifier.R_StaticModifiable;
import io.determann.shadow.api.reflection.shadow.structure.R_Constructor;
import io.determann.shadow.api.reflection.shadow.structure.R_EnumConstant;
import io.determann.shadow.api.shadow.type.C_Enum;

import java.util.List;

import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;

public interface R_Enum extends C_Enum,
                                R_Declared,
                                R_StaticModifiable
{
   List<R_EnumConstant> getEumConstants();

   List<R_Constructor> getConstructors();

   default R_EnumConstant getEnumConstantOrThrow(String simpleName)
   {
      return getEumConstants().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).findAny().orElseThrow();
   }
}
