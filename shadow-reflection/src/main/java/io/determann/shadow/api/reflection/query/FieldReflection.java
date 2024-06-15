package io.determann.shadow.api.reflection.query;

import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Field;

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
