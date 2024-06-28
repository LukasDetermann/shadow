package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.type.Enum;

public interface EnumConstantReflection extends EnumConstant,
                                                VariableReflection
{
   @Override
   Enum getSurrounding();
}
