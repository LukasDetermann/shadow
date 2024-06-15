package io.determann.shadow.api.lang_model.query;

import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.EnumConstant;

public interface EnumConstantLangModel extends EnumConstant,
                                               VariableLangModel
{
   @Override
   Enum getSurrounding();
}
