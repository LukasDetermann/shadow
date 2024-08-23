package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.type.R_Enum;
import io.determann.shadow.api.shadow.structure.C_EnumConstant;

public non-sealed interface R_EnumConstant extends C_EnumConstant,
                                                   R_Variable
{
   @Override
   R_Enum getSurrounding();
}
