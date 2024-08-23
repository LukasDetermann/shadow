package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.modifier.LM_FinalModifiable;
import io.determann.shadow.api.shadow.structure.C_Constructor;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.structure.C_Parameter;

import java.util.List;

/**
 * Parameter of a method or constructor
 *
 * @see C_Method#getParameters()
 * @see C_Constructor#getParameters()
 */
public non-sealed interface LM_Parameter extends C_Parameter,
                                                 LM_Variable,
                                                 LM_FinalModifiable
{
   /**
    * {@link List#of(Object[])}
    */
   boolean isVarArgs();

   @Override
   LM_Executable getSurrounding();
}
