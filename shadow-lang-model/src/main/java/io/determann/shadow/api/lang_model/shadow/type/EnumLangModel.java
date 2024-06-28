package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.type.Enum;

import java.util.List;

import static io.determann.shadow.api.shadow.Operations.NAMEABLE_NAME;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public interface EnumLangModel extends Enum,
                                       DeclaredLangModel
{
   List<EnumConstant> getEumConstants();

   default EnumConstant getEnumConstantOrThrow(String simpleName)
   {
      return getEumConstants().stream().filter(field -> requestOrThrow(field, NAMEABLE_NAME).equals(simpleName)).findAny().orElseThrow();
   }
}
