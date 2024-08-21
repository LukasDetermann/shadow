package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.modifier.R_AccessModifiable;
import io.determann.shadow.api.reflection.shadow.modifier.R_FinalModifiable;
import io.determann.shadow.api.reflection.shadow.modifier.R_StaticModifiable;
import io.determann.shadow.api.reflection.shadow.type.R_Declared;
import io.determann.shadow.api.shadow.structure.C_Field;

public interface R_Field extends C_Field,
                                 R_Variable,
                                 R_AccessModifiable,
                                 R_FinalModifiable,
                                 R_StaticModifiable
{
   boolean isConstant();

   /**
    * String or primitive value of static fields
    */
   Object getConstantValue();

   @Override
   R_Declared getSurrounding();
}
