package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.modifier.R_FinalModifiable;
import io.determann.shadow.api.shadow.structure.C_Parameter;

import java.util.List;

/// Parameter of a method or constructor
///
/// @see R_Executable#getParameters()
public non-sealed interface R_Parameter

      extends C_Parameter,
              R_Variable,
              R_FinalModifiable
{
   /**
    * {@link List#of(Object[])}
    */
   boolean isVarArgs();

   @Override
   R_Executable getSurrounding();
}
