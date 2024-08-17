package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.type.EnumLangModel;
import io.determann.shadow.api.shadow.structure.EnumConstant;

public interface EnumConstantLangModel extends EnumConstant,
                                               VariableLangModel
{
   @Override
   EnumLangModel getSurrounding();
}
