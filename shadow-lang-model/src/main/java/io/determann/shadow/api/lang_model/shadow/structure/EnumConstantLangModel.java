package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.type.Enum;

public interface EnumConstantLangModel extends EnumConstant,
                                               VariableLangModel
{
   @Override
   Enum getSurrounding();
}
