package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.modifier.AccessModifiableReflection;
import io.determann.shadow.api.reflection.shadow.modifier.FinalModifiableReflection;
import io.determann.shadow.api.reflection.shadow.modifier.StaticModifiableReflection;
import io.determann.shadow.api.reflection.shadow.type.DeclaredReflection;
import io.determann.shadow.api.shadow.structure.Field;

public interface FieldReflection extends Field,
                                         VariableReflection,
                                         AccessModifiableReflection,
                                         FinalModifiableReflection,
                                         StaticModifiableReflection
{
   boolean isConstant();

   /**
    * String or primitive value of static fields
    */
   Object getConstantValue();

   @Override
   DeclaredReflection getSurrounding();
}
