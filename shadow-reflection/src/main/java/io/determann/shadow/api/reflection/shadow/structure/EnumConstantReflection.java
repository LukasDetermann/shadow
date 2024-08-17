package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.type.EnumReflection;
import io.determann.shadow.api.shadow.structure.EnumConstant;

public interface EnumConstantReflection extends EnumConstant,
                                                VariableReflection
{
   @Override
   EnumReflection getSurrounding();
}
