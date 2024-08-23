package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.modifier.LM_AccessModifiable;
import io.determann.shadow.api.lang_model.shadow.modifier.LM_FinalModifiable;
import io.determann.shadow.api.lang_model.shadow.modifier.LM_StaticModifiable;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.shadow.structure.C_Field;

public non-sealed interface LM_Field extends C_Field,
                                  LM_Variable,
                                  LM_AccessModifiable,
                                  LM_FinalModifiable,
                                  LM_StaticModifiable

{
   boolean isConstant();

   /**
    * String or primitive value of static fields
    */
   Object getConstantValue();

   @Override
   LM_Declared getSurrounding();
}
