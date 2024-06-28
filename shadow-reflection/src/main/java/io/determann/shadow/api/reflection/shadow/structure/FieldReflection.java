package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.type.Declared;

public interface FieldReflection extends Field,
                                         VariableReflection
{
   boolean isConstant();

   /**
    * String or primitive value of static fields
    */
   Object getConstantValue();

   @Override
   Declared getSurrounding();
}
