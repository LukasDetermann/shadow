package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.R_Annotationable;
import io.determann.shadow.api.reflection.shadow.R_Nameable;
import io.determann.shadow.api.reflection.shadow.modifier.R_Modifiable;
import io.determann.shadow.api.reflection.shadow.type.R_VariableType;
import io.determann.shadow.api.shadow.structure.C_Variable;
import io.determann.shadow.api.shadow.type.C_Type;

public sealed interface R_Variable

      extends C_Variable,
              R_Annotationable,
              R_Nameable,
              R_ModuleEnclosed,
              R_Modifiable

      permits R_EnumConstant,
              R_Field,
              R_Parameter
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a type implements for example a
    * {@link java.util.Collection} {@code typeToTest.erasure().isSubtypeOf(context.getDeclaredOrThrow("java.util.Collection").erasure())}
    */
   boolean isSubtypeOf(C_Type type);

   /**
    * Equivalent to {@link #isSubtypeOf(C_Type)} except for primitives.
    * if one is a primitive and the other is not it tries to convert them
    */
   boolean isAssignableFrom(C_Type type);

   R_VariableType getType();

   /**
    * The {@link Object} surrounding this {@link R_Variable}
    */
   Object getSurrounding();
}
