package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.type.LM_Enum;
import io.determann.shadow.api.shadow.structure.C_EnumConstant;

public non-sealed interface LM_EnumConstant extends C_EnumConstant,
                                                    LM_Variable
{
   @Override
   LM_Enum getSurrounding();
}
