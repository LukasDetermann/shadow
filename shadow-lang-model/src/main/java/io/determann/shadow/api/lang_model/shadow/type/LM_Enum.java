package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.shadow.modifier.LM_StaticModifiable;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Constructor;
import io.determann.shadow.api.lang_model.shadow.structure.LM_EnumConstant;
import io.determann.shadow.api.shadow.type.C_Enum;

import java.util.List;

import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;

public non-sealed interface LM_Enum

      extends C_Enum,
              LM_Declared,
              LM_StaticModifiable
{
   List<LM_Constructor> getConstructors();

   List<LM_EnumConstant> getEumConstants();

   default LM_EnumConstant getEnumConstantOrThrow(String simpleName)
   {
      return getEumConstants().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).findAny().orElseThrow();
   }
}
