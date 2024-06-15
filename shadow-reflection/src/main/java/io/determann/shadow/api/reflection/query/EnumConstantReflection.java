package io.determann.shadow.api.reflection.query;

import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.EnumConstant;

public interface EnumConstantReflection extends EnumConstant,
                                                VariableReflection
{
   @Override
   Enum getSurrounding();
}
