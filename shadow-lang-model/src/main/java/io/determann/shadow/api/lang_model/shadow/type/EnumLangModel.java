package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.shadow.modifier.StaticModifiableLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.EnumConstantLangModel;
import io.determann.shadow.api.shadow.type.Enum;

import java.util.List;

import static io.determann.shadow.api.shadow.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public interface EnumLangModel extends Enum,
                                       DeclaredLangModel,
                                       StaticModifiableLangModel
{
   List<EnumConstantLangModel> getEumConstants();

   default EnumConstantLangModel getEnumConstantOrThrow(String simpleName)
   {
      return getEumConstants().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).findAny().orElseThrow();
   }
}
